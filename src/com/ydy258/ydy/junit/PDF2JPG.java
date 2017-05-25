package com.ydy258.ydy.junit;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PDF2JPG {

    private void handleText(PdfWriter writer, String content, String color,
            float x, float y, float z) {
        PdfContentByte canvas = writer.getDirectContent();
        Phrase phrase = new Phrase(content);
        if (color != null) {
            phrase = new Phrase(content, FontFactory.getFont(
                    FontFactory.COURIER, 12, Font.NORMAL));
        }

        ColumnText.showTextAligned(canvas, Element.ALIGN_UNDEFINED, phrase, x,
                y, z);
    }

    public File Pdf(String imagePath, String mOutputPdfFileName) {
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(
                    mOutputPdfFileName));
            doc.open();

            doc.newPage();
            Image png1 = Image.getInstance(imagePath);
            float heigth = png1.getHeight();
            float width = png1.getWidth();
            int percent = this.getPercent2(heigth, width);
            png1.setAlignment(Image.MIDDLE);
            png1.setAlignment(Image.TEXTWRAP);
            png1.scalePercent(percent + 3);
            doc.add(png1);
            this.handleText(writer, "This is a test", "red", 400, 725, 0);
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mOutputPdfFile = new File(mOutputPdfFileName);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        return mOutputPdfFile;
    }

    public int getPercent1(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    private int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

    public static void main(String[] args) {
        PDF2JPG gp = new PDF2JPG();
        String pdfUrl = "D:\\my_blspb1.PDF";
        File f = new File("D:\\683.JPG");
        try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        File file = gp
                .Pdf(f.getAbsolutePath(),pdfUrl);
        
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}