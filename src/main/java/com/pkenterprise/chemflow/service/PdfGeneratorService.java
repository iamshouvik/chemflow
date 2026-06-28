package com.pkenterprise.chemflow.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.pkenterprise.chemflow.model.CompanyProfile;
import com.pkenterprise.chemflow.model.Invoice;
import com.pkenterprise.chemflow.repository.CompanyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;


@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;
    private final CompanyProfileRepository companyProfileRepository;

    public byte[] generateInvoicePdf(Invoice invoice) throws Exception {

        // 1. Fetch the master company profile (Assuming ID 1 is always your main profile)
        CompanyProfile company = companyProfileRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Company Profile not found in database! Did you run the SQL insert?"));

        // 2. Put BOTH the Invoice and the Company Profile into the Thymeleaf Context box
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("company", company); // Now the HTML can access this!

        // 3. Render the HTML
        String html = templateEngine.process("invoice", context);

        // 4. Convert to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            
            // Load native Windows Arial font to support the Indian Rupee (₹) symbol
            File arialFont = new File("C:/Windows/Fonts/arial.ttf");
            if (arialFont.exists()) {
                builder.useFont(arialFont, "Arial");
            }

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        }
    }
}
