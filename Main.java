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

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import org.dom4j.DocumentException;

/**
 *
 * @author Donn W Pike
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.net.UnknownHostException
     * @throws org.dom4j.DocumentException
     * @throws java.security.GeneralSecurityException
     */
    public static void main(String args[]) throws IOException, UnknownHostException, DocumentException, GeneralSecurityException {
//        System.out.println("Main");
        doit();
    }

    /**
     *
     * @throws IOException
     * @throws UnknownHostException
     * @throws DocumentException
     * @throws java.security.GeneralSecurityException
     */
    public static void doit() throws IOException, UnknownHostException, DocumentException, GeneralSecurityException {
        // TODO code application logic here
        // Property file, vs command line for password for clients
        // password's encrypted or no clear text 
        // Need to get the e-mail address to add a project if not usinga AM
        // Encrypt all passwords and e-mail address's
        String path = System.getProperty("user.home") + File.separator + ".btm";
        File customDir = new File(path);
        String Databasepath = customDir.toString() + File.separator + "btm.db";
        File DBpath = new File(Databasepath);
        File keyFile = new File(customDir.toString() + File.separator + "btm.key");

        if (DBpath.exists()) {
            System.out.println(Databasepath + " already exists");
            UpdateDatabases.noconn(Databasepath);
        } else if (customDir.mkdirs()) {
//            System.out.println("Path " + path);
//            System.out.println(customDir + " was created");
//            CryptoUtils.encrypt("test" , keyFile);
//            System.exit(0);
        Console console = System.console();
        String password = console.readLine("Please enter password : ");
        String email = console.readLine("Please enter the e-mail to add projects for : ");

            CreateDatabase.newDatabase("192.168.0", CryptoUtils.encrypt(password, keyFile), CryptoUtils.encrypt(email, keyFile));
        } else {
            System.out.println(customDir + " was not created");
        }
    }
}
