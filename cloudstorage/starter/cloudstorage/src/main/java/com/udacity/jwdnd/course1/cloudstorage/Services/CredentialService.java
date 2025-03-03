package com.udacity.jwdnd.course1.cloudstorage.Services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, HashService hashService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        credential.setKey(encodedKey);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));

        credentialMapper.insert(credential);
    }

    public void editCredential(Credential credential) {
        Credential cred = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialId());
        credential.setKey(cred.getKey());
        String encodedPassword = this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encodedPassword);

        credentialMapper.update(credential);
    }

    public void deleteCredential(Integer id) {
        credentialMapper.delete(id);
    }

    public Object getCredentialByUserId(Integer userId) {
        return credentialMapper.getAllCredentialByUserId(userId);
    }

}
