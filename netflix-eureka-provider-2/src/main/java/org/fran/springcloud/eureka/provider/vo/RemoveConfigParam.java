package org.fran.springcloud.eureka.provider.vo;

public class RemoveConfigParam {
    private int removeType;
    private String key;
    private String version;
    private String os;
    private String channel;

    public int getRemoveType() {
        return removeType;
    }

    public void setRemoveType(int removeType) {
        this.removeType = removeType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}