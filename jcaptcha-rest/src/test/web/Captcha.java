package test.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

@Component
@Path("/")
public class Captcha {

	ImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService();
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	@GET
	@Path("captcha.jpg")
	@Produces("application/octet-stream")
	public Response getImage(@QueryParam("token") String token) throws Exception {

		BufferedImage image = imageCaptchaService.getImageChallengeForID( token );
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", os);
		final InputStream is = new ByteArrayInputStream(os.toByteArray());
		
		StreamingOutput stream = new StreamingOutput() { 
			@Override
			public void write(OutputStream os) throws IOException {
				IOUtils.copy(is, os);
			}
		};

		return Response
				.status(Status.OK)
				.header("Cache-Control","no-transform,public,max-age=300,s-maxage=900")
				.header("Content-Type", "image/jpeg")
				.entity(stream).build();
		
	}
	
	@POST
	@Path("resolve")
	@Produces("application/json")
	public Response resolve(@FormParam("token") String token, @FormParam("answer") String answer){

		Map<String,String> ret = new HashMap<String,String>();
		
		boolean validated = false;
		
		try {
			validated = imageCaptchaService.validateResponseForID(token, answer);
		}catch(Exception e){
			ret.put("exception", e.getMessage());
			e.printStackTrace();
		}
		
		ret.put("answer", answer);
		ret.put("token", token);
		ret.put("validated", ""+validated);
		
		return Response
				.status(Status.OK)
				.entity(ret)
				.build();
	}
	
}
