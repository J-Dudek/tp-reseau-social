package com.progparcomposant.reseausocial.views;

public class UserViews {
    private  UserViews(){
        super();
    }
    public static class Public{};
    public static class Friends extends Public{};
    public static class Private extends Friends{};
    public static class Admin extends Private{};

}
