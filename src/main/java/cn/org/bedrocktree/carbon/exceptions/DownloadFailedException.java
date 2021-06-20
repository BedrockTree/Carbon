package cn.org.bedrocktree.carbon.exceptions;

public class DownloadFailedException extends Exception {
    public DownloadFailedException(){
        super();
    }
    public DownloadFailedException(String msg){
        super(msg);
    }
}
