package com.cokus.fangdouyu.modle;

/**
 * Created by chenzhuo on 2017/4/20.
 */

public class GsonDouyuRoom {
    private int error;
    private Data data;

    public int getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private int room_id;
        private String live_url;
        private String hls_url;

        public String getHls_url() {
            return hls_url;
        }

        public void setHls_url(String hls_url) {
            this.hls_url = hls_url;
        }

        public int getRoom_id() {
            return room_id;
        }

        public String getLive_url() {
            return live_url;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoom [room_id=" + room_id + ", live_url" + live_url + "]";
        }
    }
}
