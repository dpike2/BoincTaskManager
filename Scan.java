/*
 * Copyright (C) 2020 Donn W Pike
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tnssdwp.btm.boinctaskmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.dom4j.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Donn W Pike
 */
public class Scan {

    private static Socket socket;
    private static OutputStreamWriter output;
    private static InputStream input;

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String MD5(String text) {
        byte[] md5hash = new byte[32];
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5hash = md.digest();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ignored) {
        }
        return convertToHex(md5hash);
    }

    /**
     *
     * @param network
     * @param passworde encrypted password
     * @param password unencrypted password
     * @param conn
     * @throws UnknownHostException
     * @throws IOException
     * @throws org.dom4j.DocumentException
     * @throws java.sql.SQLException
     * @throws java.security.GeneralSecurityException
     */
    public static void scan(String network, String passworde, String userid, Connection conn) throws UnknownHostException, IOException, DocumentException, SQLException, GeneralSecurityException {
//        System.out.println("Scan = " + network + " " + password + " ");
        String path = System.getProperty("user.home") + File.separator + ".btm";
        File customDir = new File(path);
        File keyFile = new File(customDir.toString() + File.separator + "btm.key");
        InetAddress localhost = InetAddress.getLocalHost();
        InetAddress loopback = InetAddress.getLoopbackAddress();
        String localaddr = localhost.getHostAddress();
        StringBuilder found = new StringBuilder();
        String password = CryptoUtils.decrypt(passworde, keyFile);

        try {
            int i = 0;
            while (i <= 255) {
//                System.out.println("i = " + i);
                InetAddress addr = InetAddress.getByName(network + String.valueOf(i));

                if (!serverListening(addr, 31416)) {
                    i++;
                    continue;
                }
                System.out.println(addr.getCanonicalHostName());
                Socket socket = new Socket(addr, 31416);
                InputStream input = socket.getInputStream();
                OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream(), "ISO8859_1");

                /* Need to use 127.0.0.1 or localhost for the machine that this program is running on
                * if not will error out with connection reset
                 */
                if (localaddr.endsWith(String.valueOf(i))) {
                    socket = new Socket(loopback, 31416);
//                    addr = loopback;
                    input = socket.getInputStream();
                    output = new OutputStreamWriter(socket.getOutputStream(), "ISO8859_1");
                    System.out.println("Localaddr found");
                }

                startCommand(output);
                output.write("<auth1/>");
                endCommand(output);

                Document response = DocumentHelper.parseText(read(input));
                String nonce = response.selectSingleNode("/boinc_gui_rpc_reply/nonce").getText();

                startCommand(output);
                output.write("<auth2>\n");
                output.write("<nonce_hash>" + MD5(nonce + password) + "</nonce_hash>n");
                output.write("</auth2>\n");
                endCommand(output);

                String auth = read(input);
                String saddr = String.valueOf(addr);
                if (authorized(auth)) {
                    startCommand(output);
                    output.write("<acct_mgr_info/>");
                    endCommand(output);
                    Document amsRe = DocumentHelper.parseText(read(input));
//                    System.out.println("amsRe = " + amsRe);
                    String amsName = amsRe.selectSingleNode("/boinc_gui_rpc_reply/acct_mgr_info/acct_mgr_name").getText();
                    System.out.println("amsName = " + amsName);

                    saddr = saddr.replace('/', ' ').strip();
                    Statement statement = conn.createStatement();
                    String newHost = "INSERT INTO Machines (IPAddress, Name, UserID, Password) "
                            + "VALUES ('" + saddr + "' , '" + addr.getCanonicalHostName() + "' , '" + userid + "' , '" + passworde + "');";
                    System.out.println(newHost);
                    statement.execute(newHost);
                    String getAms = "SELECT AcctMgrID from AcctMgr where Name = '" + amsName + "';";
                    System.out.println("getAms = " + getAms);
                    ResultSet rs = statement.executeQuery(getAms);
                    while (rs.next()) {
                        System.out.println("RS = " + rs.getInt(1));
                        String newAms = "UPDATE Machines set acctMgr = '" + rs.getInt(1) + " '";
                        System.out.println("newAMS = " + newAms);
                        statement.execute(newAms);
                    }
                    found.append(saddr).append("\n");
                    System.out.println("saddr = " + saddr);

                    close(input, output, socket);

                    i++;
                }
            }

        } catch (IOException ex) {
        }
//        System.out.println("Boinc clients found = \n" + found.toString());

    }

    /**
     *
     * @param host IP address of the machine
     * @param port Boinc Port 31416
     * @return true if connection, false if no connection
     */
    public static boolean serverListening(InetAddress host, int port) {
        Socket s = null;
        try {
            s = new Socket(host, port);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (s != null)
            try {
                s.close();
            } catch (Exception e) {
            }
        }
    }

    public static void startCommand(OutputStreamWriter output) throws IOException {
        output.write("<boinc_gui_rpc_request>\n");
    }

    public static void endCommand(OutputStreamWriter output) throws IOException {
        output.write("</boinc_gui_rpc_request>\003");
        output.flush();
    }

    public static void close(InputStream input, OutputStreamWriter output, Socket socket) throws IOException {
        input.close();
        output.close();
        socket.close();
    }

    public static String read(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            int value = 0;

            while ((value = input.read()) != -1) {
                if (value == '\003') {
                    break;
                }
                result.append((char) value);
            }
        } catch (IOException e) {
            result.append(e.getMessage());
        }
        System.out.println(result.toString());

        return result.toString();
    }

    public static boolean success(String response) throws IOException {
        if (response == null) {
            return false;
        }
        return response.contains("<success/>");
    }

    public static boolean authorized(String response) throws IOException {
        if (response == null) {
            return false;
        }
        return response.contains("<authorized/>");
    }

}
