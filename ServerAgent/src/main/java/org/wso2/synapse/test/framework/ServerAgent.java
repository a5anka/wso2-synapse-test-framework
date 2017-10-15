/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.synapse.test.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;
import java.nio.file.Paths;

/**
 * This is a Agent to run in synapse server machine
 *
 * @since 1.0-SNAPSHOT
 */
@Path("/synapseAgent")
public class ServerAgent {

    private static final java.nio.file.Path MOUNT_PATH = Paths.get(".");

    private ServerLogReader inputStreamHandler;
    private ServerLogReader errorStreamHandler;

    private static final Log log = LogFactory.getLog(ServerAgent.class);

    @GET
    @Path("/start")
    public void startServer() {


        Process process = null;
        try {

            File commandDir = Paths.get(MOUNT_PATH.toString()).toFile();

            String[] cmdArray;
            // For Windows
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                cmdArray = new String[]{"cmd.exe", "/c", commandDir + File.separator + "synapse.bat"};
            } else {
                // For Unix
                cmdArray = new String[]{"sh", commandDir + File.separator + "synapse.sh"};
            }

            process = Runtime.getRuntime().exec(cmdArray, null, commandDir);

            errorStreamHandler = new ServerLogReader("errorStream", process.getErrorStream());
            inputStreamHandler = new ServerLogReader("inputStream", process.getInputStream());

            // start the stream readers
            inputStreamHandler.start();
            errorStreamHandler.start();

        } catch (Exception ex) {
            log.error("Error while starting synapse server", ex);
        }

    }

    @GET
    @Path("/stop")
    public void stopServer() {

    }

}
