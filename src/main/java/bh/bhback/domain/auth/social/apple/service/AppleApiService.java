package bh.bhback.domain.auth.social.apple.service;
import bh.bhback.domain.auth.social.apple.dto.*;
import bh.bhback.global.error.advice.exception.CCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppleApiService {

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final Environment env;

    public RetAppleLoginOAuth validateRefreshToken(String refreshToken){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", refreshToken);
        params.add("client_id", env.getProperty("social.apple.client-id"));
        params.add("grant_type", "refresh_token");
        params.add("client_secret", makeClientSecret());

        String requestUrl = env.getProperty("social.apple.url.token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), RetAppleLoginOAuth.class);
        throw new CCommunicationException();
    }

    public RetAppleSignOAuth getAppleTokenInfo(AppleSignupRequestDto appleSignupRequestDto){
        try {
            String id_token = appleSignupRequestDto.getId_token();
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            JWTClaimsSet payload = (JWTClaimsSet) signedJWT.getJWTClaimsSet();

            //signature 값을 decoding하기 위한 공개키 가져오기
            String requestUrl = env.getProperty("social.apple.url.keys");
            String publicKeys = restTemplate.getForObject(requestUrl, String.class);
            log.info("publicKeys = {}", publicKeys);


            ObjectMapper objectMapper = new ObjectMapper();
            Keys keys = objectMapper.readValue(publicKeys, Keys.class);

            boolean signature = false;
            for (Key key : keys.getKeys()) {
                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);
                if (signedJWT.verify(verifier)) {
                    signature = true;
                    log.warn("복호화 성공");
                }
            }

            Date currentTime = new Date(System.currentTimeMillis());

            String aud = payload.getAudience().get(0);
            String iss = payload.getIssuer();
            String nonce = (String) payload.getClaim("nonce");

            if (!currentTime.before(payload.getExpirationTime()) || !aud.equals(env.getProperty("social.apple.client-id")) || !iss.equals(env.getProperty("social.apple.url.aud")) /* || !nonce.equals("[nonce값]") */) {
                log.error("검증 실패");
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
        } catch (JsonMappingException e) {
            log.error(e.getMessage());
        } catch (JOSEException | JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        String clientSecret = makeClientSecret();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", env.getProperty("social.apple.client-id"));
        params.add("client_secret", clientSecret);
        params.add("code", appleSignupRequestDto.getCode());
        params.add("grant_type", "authorization_code");
//        params.add("redirect_uri", "[콜백 URI]");

        String requestUrl = env.getProperty("social.apple.url.token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), RetAppleSignOAuth.class);
        throw new CCommunicationException();
    }

    private String makeClientSecret() {
        try {
            ClassPathResource resource = new ClassPathResource("secrets/AuthKey_23UPQ37ADH.p8");
            String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            Reader pemReader = new StringReader(privateKey);
            PEMParser pemParser = new PEMParser(pemReader);
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
            String clientSecret = Jwts.builder()
                    .setHeaderParam("kid", env.getProperty("social.apple.key-id")) //apple developer key id
                    .setHeaderParam("alg", "ES256")
                    .setIssuer(env.getProperty("social.apple.team-id"))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience(env.getProperty("social.apple.url.aud"))
                    .setSubject(env.getProperty("social.apple.client-id"))
                    .signWith(SignatureAlgorithm.ES256, converter.getPrivateKey(object))
                    .compact();
            return clientSecret;
        }catch(IOException e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
