package cn.corgi.meta.media.controller;

import cn.corgi.meta.base.config.MediaConfig;
import cn.hutool.core.io.FileUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author wanbeila
 * @date 2024/6/5
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/media")
public class MediaController {

    private final MediaConfig mediaConfig;

    @GetMapping("download")
    public void download(HttpServletResponse response, String filePath) {
        // todo 暂限制路径前缀
        filePath = FileUtil.normalize(filePath);
        if (!filePath.startsWith(mediaConfig.getRootPath())) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        BufferedInputStream inputStream = FileUtil.getInputStream(filePath);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = FilenameUtils.getName(filePath);
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            //读文件，写文件
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
