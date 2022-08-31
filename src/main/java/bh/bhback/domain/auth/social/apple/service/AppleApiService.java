package bh.bhback.domain.auth.social.apple.service;

import bh.bhback.domain.auth.social.apple.dto.*;
import bh.bhback.domain.auth.social.apple.dto.Key;
import bh.bhback.global.error.advice.exception.CCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
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
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.bouncycastle.jce.provider.PEMUtil;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AppleApiService {

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final Environment env;

    public RetAppleLoginOAuth validateRefreshToken(String refreshToken) {
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

    public RetAppleSignOAuth getAppleTokenInfo(AppleSignupRequestDto appleSignupRequestDto) {
//        try {
//            String id_token = appleSignupRequestDto.getId_token();
//            SignedJWT signedJWT = SignedJWT.parse(id_token);
//            JWTClaimsSet payload = (JWTClaimsSet) signedJWT.getJWTClaimsSet();
//
//            //signature 값을 decoding하기 위한 공개키 가져오기
//            String requestUrl = env.getProperty("social.apple.url.keys");
//            String publicKeys = restTemplate.getForObject(requestUrl, String.class);
//            log.info("publicKeys = {}", publicKeys);
//
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            Keys keys = objectMapper.readValue(publicKeys, Keys.class);
//
//            boolean signature = false;
//            for (Key key : keys.getKeys()) {
//                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
//                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
//                JWSVerifier verifier = new RSASSAVerifier(publicKey);
//                if (signedJWT.verify(verifier)) {
//                    signature = true;
//                    log.warn("복호화 성공");
//                }
//            }
//
//            Date currentTime = new Date(System.currentTimeMillis());
//
//            String aud = payload.getAudience().get(0);
//            String iss = payload.getIssuer();
////            String nonce = (String) payload.getClaim("nonce");
//
//            if (!currentTime.before(payload.getExpirationTime()) || !aud.equals(env.getProperty("social.apple.client-id")) || !iss.equals(env.getProperty("social.apple.url.aud")) /* || !nonce.equals("[nonce값]") */) {
//                log.error("검증 실패");
//            }
//        } catch (ParseException e) {
//            log.error(e.getMessage());
//        } catch (JsonMappingException e) {
//            log.error(e.getMessage());
//        } catch (JOSEException | JsonProcessingException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }

        String clientSecret = makeClientSecret();

        log.error("20");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", env.getProperty("social.apple.client-id"));
        params.add("client_secret", clientSecret);
        params.add("code", appleSignupRequestDto.getCode());
        params.add("grant_type", "authorization_code");
//        params.add("redirect_uri", "[콜백 URI]");

        String requestUrl = env.getProperty("social.apple.url.token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        log.error("3");

        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), RetAppleSignOAuth.class);

        log.error("4");

        throw new CCommunicationException();
    }
    private byte[] readPrivateKey(String keyPath) {

        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try (FileReader keyReader = new FileReader(resource.getFile());
             PemReader pemReader = new PemReader(keyReader)) {
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public  byte[] readKey() throws Exception {
        ClassPathResource resource = new ClassPathResource("secrets/AuthKey_23UPQ37ADH.p8");
        String content = new String(resource.getInputStream().readAllBytes(), "utf-8");
        return Base64.getDecoder().decode(content);
//        return resource.getInputStream().readAllBytes();
    }
    // kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    public String makeClientSecret() {
        long now = System.currentTimeMillis() / 1000;
        try {
            Map header = new HashMap();
            Map claims = new HashMap();
            log.info("1");
            header.put("kid", env.getProperty("social.apple.key-id")); //
            claims.put("iss", env.getProperty("social.apple.team-id")); //        team id
            claims.put("iat", now);
            claims.put("exp", now + 86400 * 30);
            claims.put("aud", env.getProperty("social.apple.url.aud")); //
            claims.put("sub", env.getProperty("social.apple.client-id"));
            log.info("2");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(readKey());
            log.info("3");
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            log.info("4");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            log.info("5");
            return Jwts.builder()
                    .setHeader(header)
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.ES256, privateKey)
                    .compact();
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    public String makeClientSecret() {
//        String keyPath = "secrets/AuthKey_23UPQ37ADH.p8";
//        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(env.getProperty("social.apple.key-id")).build();
//        JWTClaimsSet claimsSet = new JWTClaimsSet();
//        Date now = new Date();
//
//        claimsSet.setIssuer();
//        claimsSet.setIssueTime(now);
//        claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
//        claimsSet.setAudience(env.getProperty("social.apple.url.aud"));
//        claimsSet.setSubject(env.getProperty("social.apple.client-id"));
//
//        SignedJWT jwt = new SignedJWT(header, claimsSet);
//
//        try {
//            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey(keyPath));
//            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
//
//            jwt.sign(jwsSigner);
//
//        } catch (InvalidKeyException | JOSEException e) {
//            e.printStackTrace();
//        }
//
//        return jwt.serialize();
//    }

//    private String makeClientSecret() {
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            log.info("provider:{}", Security.getProvider("BC"));
//            ClassPathResource resource = new ClassPathResource("secrets/AuthKey_23UPQ37ADH.p8");
//            String contents = new String(resource.getInputStream().readAllBytes());
//            log.error("1");
//
//            Reader pemReader = new StringReader(contents);
//            PEMParser pemParser = new PEMParser(pemReader);
//            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//            log.error("10");
//
//
//            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
//            log.error("20");
//
//            Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
//            String clientSecret = Jwts.builder()
//                    .setHeaderParam("kid", env.getProperty("social.apple.key-id")) //apple developer key id
//                    .setHeaderParam("alg", "ES256")
////                    .setHeaderParam(JwsHeader.ALGORITHM, "ES256")
//                    .setIssuer(env.getProperty("social.apple.team-id"))
//                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(expirationDate)
//                    .setAudience(env.getProperty("social.apple.url.aud"))
//                    .setSubject(env.getProperty("social.apple.client-id"))
//                    .signWith(converter.getPrivateKey(object), SignatureAlgorithm.ES256)
//                    .compact();
//            log.error("100");
//            return clientSecret;
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

//
//    private String makeClientSecret() {
//        try {
//            Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
//            String clientSecret = Jwts.builder()
//                    .setHeaderParam("kid", env.getProperty("social.apple.key-id")) //apple developer key id
//                    .setHeaderParam("alg", "ES256")
//                    .setIssuer(env.getProperty("social.apple.team-id"))
//                    .setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(expirationDate)
//                    .setAudience(env.getProperty("social.apple.url.aud"))
//                    .setSubject(env.getProperty("social.apple.client-id"))
//                    .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
//                    .compact();
//            log.error("40");
//
//            return clientSecret;
//        }catch(IOException e){
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

//    public static PrivateKey getPrivateKey() throws IOException {
////        ClassPathResource resource = new ClassPathResource("secrets/AuthKey_23UPQ37ADH.p8");
////        String content = new String(resource.getInputStream().readAllBytes(), "utf-8");
////        String content = new String(resource.getInputStream().readAllBytes(), "utf-8");
////            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
////                    .replace("-----END PRIVATE KEY-----", "")
////                    .replaceAll("\\s+", "");
////        String privateKey = content;
//        ClassPathResource resource = new ClassPathResource("secrets/AuthKey.pem");
//        byte[] keyBytes = resource.getInputStream().readAllBytes();
//
//        try {
//            log.error("20");
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            log.error("30");
////            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
//            return kf.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Java did not support the algorithm:", e);
//        } catch (InvalidKeySpecException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException("Invalid key format");
//        }
//    }
}
