Ciphertext-policy attribute-based encryption based on LSSS<br/> 

Package: cpabe-lsss0.1.rar<br/> 
Developer: Kang Qian, Liu Xuejiao<br/> 
Contact: hznubruce@163.com <br/> 
Last updated: November 27, 2014<br/> 
Descriptions: <br/> 
The cpabe toolkit provides a set of programs implementing ciphertext-policy attribute-based encryption scheme based on LSSS. Our contribution is realizing the schema by Java based on. It uses jpbc-api-1.2.1.jar, jpbc-plaf-1.2.1.jar for the algebraic operations. (http://gas.dia.unisa.it/projects/jpbc/index.html)<br/> 

Reference paper:<br/> 
The scheme is implemented as described in the following paper. <br/> 
(1) Ciphertext-Policy Attribute-Based Encryption<br/> 
John Bethencourt, Amit Sahai, and Brent Waters. 28th IEEE Symposium on Security and Privacy (Oakland) , May 2006. <br/> 
(2) Ciphertext-Policy Attribute-Based Encryption: An Expressive, Efficient, and Provably<br/> 
Secure Realization. Brent Waters. 14th International Conference on Practice and Theory in Public Key Cryptography, Taormina, Italy, March 6-9, 2011. Proceedings.<br/> 
Bugs and Limitations:<br/> 
Tested but are not sure for no bugs. What’s more, the implementing of access policy analysis is not perfect as I consider. If you find any bugs, an email (or even a patch!) directed to kangqian@yuantiaotech.com, I would be appreciated.<br/> 

Instruction:<br/> 
The toolkit offers two set of interface:<br/> 
1.Read/write file<br/> 
   public void setup(String pubfile, String mkfile)<br/> 
This method generates public key and master key, result is stored in pubfile and mkfile.<br/> 
   public void keygen(String pubfile, String mkfile, String attfile, String prifile, String attList)<br/> 
This method generates attribute key and private key. The attList is a set of attribute list, like (A,B,C) and (Lily, Mike).<br/> 
   public String encrypt(String policy, String pubfile, String plaintext, String encfile)<br/> 
This method encrypts the plaintext with public key and policy. The policy reflects access structure. We can set the policy like (A and B) ,((Lily and Mike) or Boss).<br/> 
   public String decrypt(String pubfile, String attfile, String prifile, String outfile)<br/> 
This method decrypts the ciphertext with private key and attribute key, the decrypted text is stored in outfile.<br/> 

2.InterValue Class<br/> 
The class InterValue contains the attributes of pubKey, masKey, attKey, priKey, encrypted, plaintext;<br/> 
   public void setup(InterValue value)<br/> 
This method generates public key and master key. The result is stored in the object value.<br/> 
   public void keygen(InterValue value, String list)<br/> 
	This method generates attribute key and private key. The attList is a set of attribute list, like (A,B,C) and (Lily, Mike).<br/> 
Notice: The public key and master key must be valued before revoking this method.<br/> 
   public String encrypt(InterValue value , String policy, String plaintext)<br/> 
This method encrypts the plaintext with public key and policy. The policy reflects access structure. We can set the policy like (A and B) ,((Lily and Mike) or Boss). <br/> 
Notice: The public key must be valued before revoking this method.<br/> 
   public String decrypt(InterValue value)<br/> 
This method decrypts the ciphertext with private key and attribute key, the decrypted text is stored in outfile.<br/> 
Notice: The public key, attribute key, private key, and ciphertext must be valued before revoking this method.<br/> 
