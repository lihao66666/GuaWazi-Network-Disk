import java.math.BigInteger;
import java.util.Random;

public class GenerateKey {
    /// <summary>
    /// 存储RSA计算时的p
    /// </summary>
    private BigInteger p;

    /// <summary>
    /// 存储RSA计算时的q
    /// </summary>
    private BigInteger q;

    /// <summary>
    /// 存储RSA计算时的n
    /// </summary>
    private BigInteger n;

    /// <summary>
    /// 存储RSA计算时的φ(n)
    /// </summary>
    private BigInteger fai;

    /// <summary>
    /// 存储RSA计算时的e
    /// </summary>
    private BigInteger e = BigInteger.valueOf((long) 65537);

    /// <summary>
    /// 存储RSA计算时的d
    /// </summary>
    private BigInteger d;

    BigInteger RandomGenerate_512() {
        BigInteger r;
        Random random = new Random();
        r = BigInteger.probablePrime(512, random);
        return r;
    }

    BigInteger Get_d(BigInteger _fai) {
        for (BigInteger i = BigInteger.ZERO; i.min(_fai).equals(i); i=i.add(BigInteger.ONE)) {
            if (i.multiply(_fai).add(BigInteger.ONE).remainder(e).equals(BigInteger.ZERO)) {
                return i.multiply(_fai).add(BigInteger.ONE).divide(e);
            }
        }
        return BigInteger.ZERO;
    }

    public void Generate() {
        while (true) {
            p = RandomGenerate_512();
            q = RandomGenerate_512();
            if (p.equals(q)) continue;
            else {
                n = p.multiply(q);
                if (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(e).equals(BigInteger.ZERO))
                    continue;
                else {
                    fai = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
                    d = Get_d(fai);
                    if(d.equals(BigInteger.ZERO))continue;
                    break;
                }
            }
        }
    }
}
