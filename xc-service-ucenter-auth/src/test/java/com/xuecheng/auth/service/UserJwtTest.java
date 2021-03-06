package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import jdk.internal.org.objectweb.asm.Handle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt测试
 *
 * @author Kang Yong
 * @date 2022/2/1
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserJwtTest {

    /**
     * 生成一个jwt令牌
     *
     * @author Kang Yong
     * @date 2022/2/1
     */
    @Test
    public void testCreateJwt() {
        // 证书文件
        String keyLocation = "xc.keystore";
        // 秘钥库密码
        String keyStorePassword = "xuechengkeystore";
        // 访问证书路径
        ClassPathResource resource = new ClassPathResource(keyLocation);
        // 秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keyStorePassword.toCharArray());
        String keypassword = "xuecheng";
        // 秘钥别名
        String alias = "xckey";
        // 秘钥对（秘钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        // 私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "mrt");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        // 生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivateKey));
        // 取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }

    /**
     * 资源服务使用公钥验证jwt的合法性，并对jwt解码
     *
     * @author Kang Yong
     * @date 2022/2/2
     */
    @Test
    public void testVerify() {
        // jwt令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Im1ydCIsImlkIjoiMTIzIn0.a3pPI4RIGiI-4gYmC5aO_5iqsVaigKb_vSmjTxx-AjtujjBTENxqt6OI4K9ppx0IO3WEebBeOKQr6po-PnQvmMl7aNHPd9KWfKD_he_0Im88nXWU2JBX0ARc85rFiIB190r7kjFqlZrj7YkhwUsccC7PqfiyKg7Y6B7Ca_l98Dx3Zv6VJwYxVU159XlB6G2NGMIRmDbJhnxLUG0zZdOKBs0BsQobU-IRRCI2bEu5zTImimfogNsGciTCS-7CMN8kO4T_rSMwEe66vPMA-IjchsubfseIptYNVu9_QHbc8RNGf3wWrBcZOmTxaCIH-zAo02u1TQXyeeafLQQVzRTt8g";
        // 公钥
        String publickey = "";
        // 校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        // 获取jwt原始内容
        String claims = jwt.getClaims();
        // jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
