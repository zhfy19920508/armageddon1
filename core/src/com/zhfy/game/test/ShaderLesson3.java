package com.zhfy.game.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * A port of ShaderLesson3 from lwjgl-basics to LibGDX:
 * https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson3
 *
 * @author davedes
 */
public class ShaderLesson3 implements ApplicationListener {

    //Minor differences:
    //LibGDX Position attribute is a vec4
    //u_projView is called u_projTrans
    //we need to set ShaderProgram.pedantic to false
    //LibGDX uses lower-left as origin (0, 0)
    //TexCoord attribute requires "0" appended at end to denote GL_TEXTURE0
    //ShaderProgram.TEXCOORD_ATTRIBUTE+"0"
    //It's wise to use LOWP when possible in GL ES, for values between 0-1
    //In LibGDX ShaderProgram uses begin() and end()


    final String VERT =
            "attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
                    "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
                    "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";


    //This will be dumped to System.out for clarity
    final String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "//texture 0\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "//our screen resolution, set from Java whenever the display is resized\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "//\"in\" attributes from our vertex shader\n" +
                    "varying LOWP vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +
                    "\n" +
                    "//RADIUS of our vignette, where 0.5 results in a circle fitting the screen\n" +
                    "const float RADIUS = 0.75;\n" +
                    "\n" +
                    "//softness of our vignette, between 0.0 and 1.0\n" +
                    "const float SOFTNESS = 0.45;\n" +
                    "\n" +
                    "//sepia colour, adjust to taste\n" +
                    "const vec3 SEPIA = vec3(1.2, 1.0, 0.8); \n" +
                    "\n" +
                    "void main() {\n" +
                    "	//sample our texture\n" +
                    "	vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
                    "		\n" +
                    "	//1. VIGNETTE\n" +
                    "	\n" +
                    "	//determine center position\n" +
                    "	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);\n" +
                    "	\n" +
                    "	//determine the vector length of the center position\n" +
                    "	float len = length(position);\n" +
                    "	\n" +
                    "	//use smoothstep to create a smooth vignette\n" +
                    "	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);\n" +
                    "	\n" +
                    "	//apply the vignette with 50% opacity\n" +
                    "	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);\n" +
                    "		\n" +
                    "	//2. GRAYSCALE\n" +
                    "	\n" +
                    "	//convert to grayscale using NTSC conversion weights\n" +
                    "	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
                    "	\n" +
                    "	//3. SEPIA\n" +
                    "	\n" +
                    "	//create our sepia tone from some constant value\n" +
                    "	vec3 sepiaColor = vec3(gray) * SEPIA;\n" +
                    "		\n" +
                    "	//again we'll use mix so that the sepia effect is at 75%\n" +
                    "	texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75);\n" +
                    "		\n" +
                    "	//final colour, multiplied by vertex colour\n" +
                    "	gl_FragColor = texColor * vColor;\n" +
                    "}";

    Texture tex;
    SpriteBatch batch;
    OrthographicCamera cam;
    ShaderProgram shader;

    @Override
    public void create() {

        //the texture does not matter since we will ignore it anyways
        tex = new Texture(Gdx.files.internal("data/libgdx.png"));

        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        //print it out for clarity
        System.out.println("Vertex Shader:\n-------------\n\n"+VERT);
        System.out.println("\n");
        System.out.println("Fragment Shader:\n-------------\n\n"+FRAG);

        shader = new ShaderProgram(VERT, FRAG);
        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }
        if (shader.getLog().length()!=0)
            System.out.println(shader.getLog());


        batch = new SpriteBatch(1000, shader);
        batch.setShader(shader);

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false);
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);

        //bind the shader, then set the uniform, then unbind the shader
        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
        tex.dispose();
    }
}