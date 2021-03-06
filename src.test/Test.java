import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import jp.co.systembase.barcode.Codabar;
import jp.co.systembase.barcode.Code128;
import jp.co.systembase.barcode.Code39;
import jp.co.systembase.barcode.Ean13;
import jp.co.systembase.barcode.Ean8;
import jp.co.systembase.barcode.Gs1_128;
import jp.co.systembase.barcode.Itf;
import jp.co.systembase.barcode.Yubin;

public class Test {

	public static void main(String[] args) {
		{
			BufferedImage i = new BufferedImage(2000, 3000, BufferedImage.TYPE_INT_RGB);
			Graphics g = i.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 2000, 3000);
			{
				Ean8 b = new Ean8();
				b.render(g, 100, 100, 500, 200, "4901234");
				b.render(g, 100, 400, 500, 200, "5678901");
				b.render(g, 100, 700, 500, 200, "5678901");
				b.withText = false;
				b.render(g, 100, 1000, 500, 200, "8888888");
			}
			{
				Ean13 b = new Ean13();
				b.render(g, 700, 100, 500, 200, "490123456789");
				b.render(g, 700, 400, 500, 200, "192205502800");
				b.render(g, 700, 700, 500, 200, "978488337649");
				b.withText = false;
				b.render(g, 700, 1000, 500, 200, "390123456789");
			}
			{
				Codabar b = new Codabar();
				b.render(g, 1300, 100, 600, 200, "A123456A");
				b.render(g, 1300, 400, 600, 200, "B987653B");
				b.generateCheckSum = true;
				b.render(g, 1300, 700, 600, 200, "C12-$:/.+34C");
				b.withText = false;
				b.render(g, 1300, 1000, 600, 200, "D98-$:/.+21D");
			}
			{
				Code128 b = new Code128();
				b.render(g, 100, 1500, 500, 200, "012345");
				b.render(g, 100, 1800, 500, 200, "!@#$%^");
				b.render(g, 100, 2100, 500, 200, "[]{};'");
				b.withText = false;
				b.render(g, 100, 2400, 500, 200, "abcd1h");
			}
			{
				Code39 b = new Code39();
				b.render(g, 700, 1500, 500, 200, "012345");
				b.render(g, 700, 1800, 500, 200, "ABCDEF");
				b.generateCheckSum = true;
				b.render(g, 700, 2100, 500, 200, "KLMNOP");
				b.withText = false;
				b.render(g, 700, 2400, 500, 200, ". $/+%");
			}
			{
				Itf b = new Itf();
				b.render(g, 1300, 1500, 500, 200, "12345678901231");
				b.render(g, 1300, 1800, 500, 200, "14901234567891");
				b.generateCheckSum = true;
				b.render(g, 1300, 2100, 500, 200, "1234567890123");
				b.withText = false;
				b.render(g, 1300, 2400, 500, 200, "1234567890123");
			}
			try {
				ImageIO.write(i, "png", new File("output/test.png"));
			} catch (Exception e) {
			}
		}

		{
			BufferedImage i = new BufferedImage(2000, 3000, BufferedImage.TYPE_INT_RGB);
			Graphics g = i.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 2000, 3000);
			{
				Yubin b = new Yubin();
				b.render(g, 100, 100, 700, 50, "1234567890-");
				b.render(g, 100, 400, 700, 50, "ABCDEFGHIJKLMNOPQRST");
				b.render(g, 100, 700, 700, 50, "UVWXYZ");
				b.render(g, 100, 1000, 700, 50, "024007315-10-3");
				b.render(g, 100, 1300, 700, 50, "91000673-80-25J1-2B");
			}
			{
				Gs1_128 b = new Gs1_128();
				b.render(g, 1100, 100, 800, 200, "(00)123456789012345678");
				b.render(g, 1100, 400, 800, 200, "(11)ABCDEF{1}(99)!\"%&'()*+,-./");
				b.withText = false;
				b.render(g, 1100, 700, 800, 200, "(01)04912345123459(10)ABC123");
				b.withText = true;
				b.conveniFormat = true;
				b.render(g, 1100, 1000, 800, 200, "(91)12345678901234567890123456789012345678901");
			}
			try {
				ImageIO.write(i, "png", new File("output/test2.png"));
			} catch (Exception e) {
			}
		}
	}
}
