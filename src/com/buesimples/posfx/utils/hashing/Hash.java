package com.buesimples.posfx.utils.hashing;

import org.mindrot.jbcrypt.BCrypt;

public class Hash {

   public static boolean verify(String plainPassword, String hashedPassword) {

      if (hashedPassword.startsWith("$2y$")) {
         hashedPassword = hashedPassword.replaceFirst("\\$2y\\$", "\\$2a\\$");
      }

      return BCrypt.checkpw(plainPassword, hashedPassword);
   }

}
