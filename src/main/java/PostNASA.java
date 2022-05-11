import java.lang.reflect.Array;

public class PostNASA {
    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String media_type;
    private String service_version;
    private String title;
    private String url;

    public String getCopyright() {
        return copyright;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getService_version() {
        return service_version;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public String toString() {
        return "PostNASA{" +
                "copyright='" + copyright + '\'' +
                ", date='" + date + '\'' +
                ", explanation='" + explanation + '\'' +
                ", hdurl='" + hdurl + '\'' +
                ", media_type='" + media_type + '\'' +
                ", service_version='" + service_version + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getFileName() {
        String[] s = url.split("/");
        //  return s[s.length -1];
        if (media_type.equals("image")) {
            return s[s.length - 1];
        } else {
            String s1 = s[s.length - 1];
            String[] res = s1.split("\\?");
            return res[0] + ".jpg";
        }
    }
}
