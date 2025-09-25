package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Step;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import com.backend.weeklybite.dto.ingredient.IngredientWithQuantityDTO;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

@Service
public class PdfService {

    public byte[] generateWeeklyRecipesPdf(Collection<GetRecipeDTO> recipes) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            Paragraph title = new Paragraph("Weekly Recipe Plan", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            for (GetRecipeDTO recipe : recipes) {

                document.add(new Paragraph("Recipe: " + recipe.getName(), headerFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("⏱ Duration: " + recipe.getDuration() + " min", normalFont));
                document.add(new Paragraph("👥 Serves: " + recipe.getNumberOfPeople() + " people", normalFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Ingredients:", boldFont));
                for (GetIngredientDTO ingredient : recipe.getProducts()) {
                    document.add(new Paragraph(" - " + ingredient.getName() + ": "
                            + ingredient.getQuantity() + " " + ingredient.getUnit(), normalFont));
                }
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Preparation Steps:", boldFont));
                for (Step step : recipe.getDescription()) {
                    document.add(new Paragraph(step.getName(), boldFont));
                    document.add(new Paragraph(step.getDescription(), normalFont));
                    document.add(new Paragraph(" "));
                }

                document.add(new Paragraph(" "));
                document.add(new Paragraph("------------------------------------------------------------"));
                document.add(new Paragraph(" "));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] generateWeeklyIngredientsPdf(Collection<IngredientWithQuantityDTO> ingredients) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Ingredients List", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {3f, 1.5f, 1.5f};
            table.setWidths(columnWidths);

            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Quantity", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Unit", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            for (IngredientWithQuantityDTO ingredient : ingredients) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(ingredient.getName(), cellFont));
                cell.setPaddingLeft(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(ingredient.getQuantity()), cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(ingredient.getUnit(), cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
