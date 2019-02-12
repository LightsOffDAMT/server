package ru.lightsoff.server.entities;

/**
 * User entity
 */
public class User {
    private Long id;
    private String login;
    private String password;
    private String email;
    private String nickname;
    private Boolean online; // mb isOnline?

    public User(){
        super();
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getOnline() {
        return online;
    }

    public User withId(Long id){
        this.id = id;
        return this;
    }

    public User withLogin(String login){
        this.login = login;
        return this;
    }

    public User withPassword(String password){
        this.password = password;
        return this;
    }

    public User withEmail(String email){
        this.email = email;
        return this;
    }

    public User withNickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    public User online(Boolean online){
        this.online = online;
        return this;
    }
}
