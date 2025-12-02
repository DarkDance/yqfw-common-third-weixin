package cn.jzyunqi.common.third.weixin.pay.tool;

import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * @author wiiyaya
 * @since 2021/7/11.
 */
public class WxPayCertPrinter {

    /**
     * 打印微信支付证书信息
     *
     * @param p12CertPath p12格式的证书路径
     * @param merchantId  商户号作为密码，如果修改过，就使用修改后的密码
     */
    public static void print(String p12CertPath, String merchantId) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(new File(p12CertPath));
        try {
            keyStore.load(fis, merchantId.toCharArray());
        } finally {
            fis.close();
        }
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate cert = keyStore.getCertificate(alias);
            PublicKey pubkey = cert.getPublicKey();
            Key key = keyStore.getKey(alias, merchantId.toCharArray());

            System.out.println("证书名(alias): " + alias);
            System.out.println("证书序列(SerialNumber): " + ((X509Certificate) cert).getSerialNumber().toString(16).toUpperCase());
            System.out.println("证书公钥(pubkey): " + DigestUtilPlus.Base64.encodeBase64String(pubkey.getEncoded()));
            System.out.println("证书私钥(prikey): " + DigestUtilPlus.Base64.encodeBase64String(key.getEncoded()));
        }
    }

    public static void printCert(String certPath) throws Exception {
        //1. 从上述数据中获取加密后的证书
        String ciphertext = "123";
        String merchantAesKey = "123";
        String nonce = "123";
        String associatedData = "123";

        //2. 使用AES-GCM解密得到微信支付的服务器证书
        String weixinServerPem = DigestUtilPlus.AES.decryptGCM(
                DigestUtilPlus.Base64.decodeBase64(ciphertext),
                merchantAesKey.getBytes(StringUtilPlus.UTF_8),
                nonce.getBytes(StringUtilPlus.UTF_8),
                associatedData.getBytes(StringUtilPlus.UTF_8)
        );

        System.out.println(weixinServerPem);

        //3. 通过服务器证书获取服务器支付公钥
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(weixinServerPem.getBytes("utf-8")));
        try {
            x509Cert.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {

        }

        PublicKey weixinServerPublicKey = x509Cert.getPublicKey();
        String weixinServerPublicKeyStr = DigestUtilPlus.Base64.encodeBase64String(weixinServerPublicKey.getEncoded());
        System.out.println("weixinServerPublicKeyStr:" + weixinServerPublicKeyStr);

        //4. 使用得到的公钥，验证当前消息的签名是否正确（这里仅仅是测试，实际上如果是第一次获取证书，使用这个方法没有意义，因为有可能证书就是伪造的，签名一定是正确的）
        String signature = "123";

        String nonce2 = "123";
        String timestamp = "1625984027";
        String bodyS = "123";
        String waitSign = String.format("%s\n%s\n%s\n", timestamp, nonce2, bodyS);
        boolean rst = DigestUtilPlus.RSA.verifyWithSHA256(waitSign.getBytes(StringUtilPlus.UTF_8), signature, DigestUtilPlus.Base64.decodeBase64(weixinServerPublicKeyStr));
        System.out.println(rst);
    }
}
