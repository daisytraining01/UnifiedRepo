package com.maveric.core.utils.ssh;

import com.jcraft.jsch.*;
import lombok.Builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Builder
public class JschUtils {
    private Session session;
    private String username;
    private String password;
    private String host;
    private int port;
    private String publicKeyPath;
    private String publicKeyPassphrase;
    private Channel channel;
    private JSch ssh;

    public boolean createSession() {
        try {
            ssh = new JSch();
            Session session = ssh.getSession(username, host, port);

            if (publicKeyPath != null && !publicKeyPath.isEmpty()) {
                if (publicKeyPassphrase != null && !publicKeyPassphrase.isEmpty())
                    ssh.addIdentity(publicKeyPath, publicKeyPassphrase);
                else
                    ssh.addIdentity(publicKeyPath);
            } else {
                session.setPassword(password);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            this.session = session;
            return true;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String executeCommand(String cmd) {

        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.connect(30000);
            StringBuilder sBuilder = new StringBuilder();
            String read = reader.readLine();
            while (read != null) {
                sBuilder.append(read);
                sBuilder.append("\n");
                read = reader.readLine();
            }
            channel.disconnect();
            return sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeSession() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null)
            session.disconnect();
    }

    public boolean fileUpload(String localFile, String remoteDir) {
        try {
            File file = new File(localFile);
            remoteDir = remoteDir.replace(" ", "_");
            String destination = remoteDir.concat("/").concat(file.getName());
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            sftp.cd(remoteDir);
            sftp.put(localFile, destination);
            sftp.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fileDownload(String remoteFile, String localFile) {
        try {
            File file = new File(localFile);
            if (file.exists() && file.isFile()) {

                ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
                sftp.connect();
                sftp.get(remoteFile, localFile);
                sftp.disconnect();
                return true;
            } else {
                throw new RuntimeException(localFile + "dosen't exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fileExist(String destination) {
        String val = executeCommand("[ -f " + destination + " ]  && echo 1 || echo 0 ");
        if (val != null) {
            val = val.replace("\n", "");
            return Long.parseLong(destination) > 0;
        }
        return false;
    }

    public List<String> listFiles(String remotePath) {
        String contentListAsString = executeCommand("find " + remotePath + " -maxdepth 1");
        return Arrays.asList(contentListAsString.split("\n"));
    }

    public void makeDir(String destination) {
        executeCommand("mkdir -p " + destination);
    }

    public void renameFile(String name, String destName) {
        name = name.replace(" ", "\\ ");
        executeCommand("mv " + name + " " + destName);
    }

}
