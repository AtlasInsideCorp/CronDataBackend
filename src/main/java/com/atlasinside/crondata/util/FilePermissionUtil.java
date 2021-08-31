package com.atlasinside.crondata.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class FilePermissionUtil {

    public FilePermissionUtil() {
    }

    public void setPathPermission(String pathPermission) throws IOException {
        Path path = Paths.get(pathPermission);
        if (!Files.exists(path)) Files.createFile(path);
        Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        Files.setPosixFilePermissions(path, perms);
        System.out.format("Permissions after:  %s%n", PosixFilePermissions.toString(perms));
    }

}
