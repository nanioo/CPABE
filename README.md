Ciphertext-policy attribute-based encryption based on LSSS

Package: cpabe-lsss0.1.rar
Developer: Kang Qian, Liu Xuejiao
Contact: kangqian@yuantiaotech.com 
Last updated: November 27, 2014
Descriptions: 
The cpabe toolkit provides a set of programs implementing ciphertext-policy attribute-based encryption scheme based on LSSS. Our contribution is realizing the schema by Java based on. It uses jpbc-api-1.2.1.jar, jpbc-plaf-1.2.1.jar for the algebraic operations. (http://gas.dia.unisa.it/projects/jpbc/index.html)

Reference paper:
The scheme is implemented as described in the following paper. 
(1) Ciphertext-Policy Attribute-Based Encryption 
John Bethencourt, Amit Sahai, and Brent Waters. 28th IEEE Symposium on Security and Privacy (Oakland) , May 2006. 
(2) Ciphertext-Policy Attribute-Based Encryption: An Expressive, Efficient, and Provably 	Secure Realization. Brent Waters. 14th International Conference on Practice and Theory in Public Key Cryptography, Taormina, Italy, March 6-9, 2011. Proceedings.
Bugs and Limitations:
Tested but are not sure for no bugs. What’s more, the implementing of access policy analysis is not perfect as I consider. If you find any bugs, an email (or even a patch!) directed to kangqian@yuantiaotech.com, I would be appreciated.

Instruction:
The toolkit offers two set of interface:
1.Read/write file
   public void setup(String pubfile, String mkfile)
This method generates public key and master key, result is stored in pubfile and mkfile.
   public void keygen(String pubfile, String mkfile, String attfile, String prifile, String attList)
This method generates attribute key and private key. The attList is a set of attribute list, like (A,B,C) and (Lily, Mike).
   public String encrypt(String policy, String pubfile, String plaintext, String encfile)
This method encrypts the plaintext with public key and policy. The policy reflects access structure. We can set the policy like (A and B) ,((Lily and Mike) or Boss).
   public String decrypt(String pubfile, String attfile, String prifile, String outfile)
	This method decrypts the ciphertext with private key and attribute key, the decrypted text is stored in outfile.

2.InterValue Class
The class InterValue contains the attributes of pubKey, masKey, attKey, priKey, encrypted, plaintext;
   public void setup(InterValue value)
This method generates public key and master key. The result is stored in the object value.
   public void keygen(InterValue value, String list)
	This method generates attribute key and private key. The attList is a set of attribute list, like (A,B,C) and (Lily, Mike).
Notice: The public key and master key must be valued before revoking this method.
   public String encrypt(InterValue value , String policy, String plaintext)
This method encrypts the plaintext with public key and policy. The policy reflects access structure. We can set the policy like (A and B) ,((Lily and Mike) or Boss). 
Notice: The public key must be valued before revoking this method.
   public String decrypt(InterValue value)
	This method decrypts the ciphertext with private key and attribute key, the decrypted text is stored in outfile.
Notice: The public key, attribute key, private key, and ciphertext must be valued before revoking this method.
