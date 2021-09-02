package org.launchcode.ouchy.models.User;

public class RegisterFormDTO extends LoginFormDTO{

    private String verifyPassword;

    private Boolean Provider;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public Boolean getProvider() {
        return Provider;
    }

    public void setProvider(Boolean provider) {
        this.Provider = provider;
    }
}
