package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginSignupDBManagerTest {

    @Test
    void samePasswordShouldBeEncryptedDifferently() {
        String out1 = LoginSignupDBManager.passwordEncryption("prova1");
        String out2 = LoginSignupDBManager.passwordEncryption("prova1");
        Assertions.assertEquals(out1, out2);
    }
}