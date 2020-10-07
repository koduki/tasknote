/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 *
 * @author koduki
 */
public class ImageFormBean {

    private byte[] file = null;

    public ImageFormBean() {
    }

    public ImageFormBean(Map<String, List<InputPart>> form) throws IOException {
        var data = form.get("file").get(0);
        this.file = data.getBody(InputStream.class, null).readAllBytes();
    }

    public byte[] getFile() {
        return file;
    }

}
