package com.special.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.special.diagrams.ClassDiagramFactory;
import com.special.diagrams.UseCaseFactory;
import com.special.domain.Project;
import com.special.services.ProjectServices;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;


@Controller
@RequestMapping("/projects/{projectId}/diagrams")
public class DiagramController {
    private final ProjectServices projectServices;
    private final UseCaseFactory useCaseFactory;
    private final ClassDiagramFactory classDiagramFactory;

    public DiagramController(ProjectServices projectServices,UseCaseFactory useCaseFactory, ClassDiagramFactory classDiagramFactory) {
        this.projectServices=projectServices;
        this.useCaseFactory = useCaseFactory;
        this.classDiagramFactory = classDiagramFactory;
    }

    @GetMapping("/usecase")
    public String useCaseDiagram(@PathVariable Long projectId, @RequestParam String tool, Model model){
        Project project=projectServices.getProject(projectId);
        String source = useCaseFactory.create(tool).create(project);

        model.addAttribute("projectId", projectId);
        model.addAttribute("diagramTitle","Use Case Diagram ("+tool+")");
        model.addAttribute("diagramSource", source);
        model.addAttribute("imageUrl",buildImageUrl(tool,source));
        return "diagram/view";

    }

    @GetMapping("/class")
    public String classDiagram(@PathVariable Long projectId, @RequestParam String tool, Model model){
        Project project=projectServices.getProject(projectId);
        String source = classDiagramFactory.create(tool).create(project);

        model.addAttribute("projectId", projectId);
        model.addAttribute("diagramTitle","Class Diagram ("+tool+")");
        model.addAttribute("diagramSource", source);
        model.addAttribute("imageUrl",buildImageUrl(tool,source));
        return "diagram/view";
    }

    //gia text se image url
    private String buildImageUrl(String tool,String source){
        if (source == null || source.isBlank()){
            return null;
        }
        return switch (tool.toLowerCase()) {
            case "plantuml" -> "https://www.plantuml.com/plantuml/png/~1" + encodePlantUML(source);
            case "nomnoml" -> "https://nomnoml.com/image.svg?source=" + URLEncoder.encode(source, StandardCharsets.UTF_8);
            default -> null;
        };
    }

    private String encodePlantUML(String source) {
        // PlantUML encoding: deflate + base64
        try {
            Deflater deflater= new Deflater(Deflater.BEST_COMPRESSION);
            deflater.setInput(source.getBytes(StandardCharsets.UTF_8));
            deflater.finish();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                out.write(buffer, 0, count);
            }
            deflater.end();
            return encode64(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode PlantUML source", e);
        }
    }

    private String encode64(byte[] data){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 3) {
            int b0= data[i] & 0xFF;
            int b1 = (i + 1 < data.length) ? data[i + 1] & 0xFF : 0;
            int b2 = (i + 2 < data.length) ? data[i + 2] & 0xFF : 0;

            sb.append(append3(b0,b1,b2));
        }
        return sb.toString();
    }

    private String append3(int b1,int b2,int b3){
        int c1 = b1 >> 2;
        int c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
        int c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
        int c4 = b3 & 0x3F;

        return "" + encode6bit(c1 & 0x3F) + encode6bit(c2 & 0x3F) + encode6bit(c3 & 0x3F) + encode6bit(c4 & 0x3F);
    }

    private char encode6bit(int b) {
        if (b<10) return (char)('0'+b);
        b -= 10;
        if (b<26) return (char)('A'+b);
        b -= 26;
        if (b<26) return (char)('a'+b);
        b-=26;
        if (b==0) return '-';
        if (b==1) return '_';
        return '?';
    }
}
