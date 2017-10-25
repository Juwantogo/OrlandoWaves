package com.juwan.orlandowaves.toAccess;

import static android.R.attr.name;
import static com.juwan.orlandowaves.R.id.phone;
import static com.juwan.orlandowaves.toAccess.Config.fbc;

/**
 * Created by Juwan on 10/20/2017.
 */

public class users {
        private String user_id;
        private long phone_number;
        private String email;
        private String fullname;
        private long auth = 0;
        private long fbc = 0;

        public users(String user_id, long phone_number, String email, String fullname) {
            this.user_id = user_id;
            this.phone_number = phone_number;
            this.email = email;
            this.fullname = fullname;
        }

        public users() {

        }


        public String getUserid() {
            return user_id;
        }

        public void setUserid(String user_id) {
            this.user_id = user_id;
        }

        public long getPhonenumber() {
            return phone_number;
        }

        public void setPhonenumber(long phone_number) {
            this.phone_number = phone_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

    public long getAuth() {
        return auth;
    }

    public void setAuth(long auth) {this.auth = auth;}

    public long getFbc() {
        return fbc;
    }
    public void setFbc(long fbc) {this.fbc = fbc;}

        @Override
        public String toString() {
            return "user_accounts{" +
                    "auth='" + auth + '\'' +
                    ", email='" + email + '\'' +
                    ", fbc='" + fbc + '\'' +
                    ", fullname='" + fullname + '\'' +
                    ", phone='" + phone + '\'' +
                    ", userid='" + user_id + '\'' +
                    '}';
        }

}
