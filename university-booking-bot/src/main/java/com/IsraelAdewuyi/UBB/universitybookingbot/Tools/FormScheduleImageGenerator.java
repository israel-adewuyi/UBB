package com.IsraelAdewuyi.UBB.universitybookingbot.Tools;

import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Service
public class FormScheduleImageGenerator {
    @Autowired
    private BookingController bookingController;
    public FileInputStream formScheduleImage(LocalDate date) throws IOException {
        // Constants for schedule layout
        int xbase = 48;
        int ybase = 73;
        int xsize = 176;
        int ysize = 32;

        // Load base image
        File imageFile = new File("C:\\Users\\isist\\Documents\\Java_Programs\\university-booking-bot\\university-booking-bot\\src\\main\\java\\com\\IsraelAdewuyi\\UBB\\universitybookingbot\\to_paint_form1280.png");
        Image image = ImageIO.read(imageFile);
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Colors, brushes, and fonts
        Color lightGreen = new Color(123, 209, 72);
        Color lightGray = Color.lightGray;
        Color payerColor = lightGreen;
        Color requesterColor = Color.BLUE;
        Color bookingColor;
        Color red = Color.red;
        Color black = Color.black;
        Font fontSimple = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        Font fontBold = new Font(Font.SANS_SERIF, Font.BOLD, 10);

        // Retrieve bookings for the week
        List<Booking> bookings = bookingController.getBookingsByDate(date);

        // Iterate over bookings
        for (Booking booking : bookings) {
//            Font currentFont = booking.getUser().getEmail()
//            Font currentFont = booking.getParticipant().getAlias().equals(participant.getAlias()) ? fontBold : fontSimple;

//            bookingColor = booking.getParticipant().getStatus().equals("free") ? lightGray : payerColor;

//            int day = BookCommand.dayOfWeekInt(booking.getTimeStart());
            int day = 1;
            Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
            long minutes = duration.toMinutes();
            int ylength = (int) (ysize * (minutes / 60f));
            int xcorner = xbase + xsize * day;
            int ycorner = ybase + (int) (ysize * ((booking.getStartTime().getHour() - 7)
                    + (booking.getStartTime().getMinute() / 60f)));

            graphics.setColor(lightGray);
            graphics.fillRect(xcorner, ycorner, xsize - 10, ylength - 5);

            String caption = booking.getUser().getEmail();
//            if (booking.getEndTime().subtract(booking.getTimeStart()).toHours() > 1) {
//                caption += "\n";
//            } else {
//                caption += " ";
//            }
            caption += booking.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " "
                    + booking.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            graphics.setColor(black);
            graphics.setFont(fontSimple);
            drawString(graphics, caption, xcorner + 2, ycorner + 2);

            // Unused string: String bookingDetails = String.format("%s %s %s\n", booking.getParticipant().getAlias(),
            // booking.getTimeStart(), booking.getTimeEnd());
        }

//        // Highlight current time
//        int nowxcorner = xbase + xsize * BookCommand.dayOfWeekInt(LocalDate.now());
//        int nowycorner = (int) (ybase + ysize * ((LocalDateTime.now().getHour() - 7 + 3)
//                + (LocalDateTime.now().getMinute() / 60f)));
//        graphics.setColor(red);
//        graphics.fillRect(nowxcorner, nowycorner, xsize, 2);

        // Save modified image
        File resultFile = new File("result.png");
        ImageIO.write(bufferedImage, "png", resultFile);

        // Return FileInputStream for the saved image
        FileInputStream fis = new FileInputStream(resultFile);
        return fis;
    }

    private static void drawString(Graphics2D g2d, String text, int x, int y) {
        FontRenderContext context = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, g2d.getFont(), context);
        textLayout.draw(g2d, x, y);
    }
}
