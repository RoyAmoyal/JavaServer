package bgu.spl.net.bgrs.users;

public abstract class User {
    private String userName;
    private String password;
    private boolean login;

    public User(String userName,String password)
    {
        this.userName = userName;
        this.password = password;
        login = false;
    }

    //The next methods are common to both Admin and Student users.
    public boolean isLoggedIn(){
        return login;
    }

    public void logIn(){
        login = true;
    }

    public void logOut(){
        login = false;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }


    //additional methods that not required
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setUserName(String newUserName){
        this.userName = newUserName;
    }

}
