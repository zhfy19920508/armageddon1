package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlurUtils {

    private BlurUtils() {
        throw new IllegalStateException("BlurUtils class");
    }

    public  final static float MAX_BLUR = 5.0F;     // 最大的模糊系数
    private static SpriteBatch blurBatch;
    private static String vertexShader;            // 定点
    private static String fragmentShader;            // 片段
    private static ShaderProgram shader;            // 自定义着色器
    private static FrameBuffer frameBufferA;        // 纹理缓存A，实际上就是用来存放上一次纹理缓存的拷贝
    private static FrameBuffer frameBufferB;        // 纹理缓存B
    private static float radius = 0.0F;                // 初始的模糊系数
    private static int fbo_size = 1024;    // 纹理缓存大小
    private static float  blur = 0.0F;        // 模糊系数
    private static  float time = 0;            // 偏移的时间
    private static float xOffset = 0.8F;    // x轴偏移，水平渲染
    private static float yOffset = 0.8F;    // y轴偏移，垂直渲染

    static {
        //定点
        vertexShader = "uniform mat4  u_projTrans;\n "
                + "attribute vec2 a_position;\n "
                + "attribute vec2 a_texCoord0;\n"
                + "attribute vec4 a_color;\n"
                + "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                + "void main() {\n"
                + "    vColor = a_color;\n"
                + "    vTexCoord = a_texCoord0;\n"
                + "    gl_Position = u_projTrans * vec4(a_position, 0.0, 1.0);\n"
                + "}";
        //片段
        fragmentShader = "varying vec4 vColor;\n"
                + "varying vec2 vTexCoord;\n"
                +"uniform sampler2D u_texture;\n"
                + "uniform float resolution;\n"
                + "uniform float radius;\n"
                + "uniform vec2 dir;\n"
                + "void main() {\n"
                + "    vec4 sum = vec4(0.0);\n"
                + "    vec2 tc = vTexCoord;\n"
                + "    float blur = radius/resolution; \n"
                + "    float hstep = dir.x;\n"
                + "    float vstep = dir.y;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;\n"
                + "    sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;\n"
                + "    gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + "}";

//        vertexShader = Gdx.files.internal("blur/vertex.vert").readString(); // 读取定点着色
//        fragmentShader = Gdx.files.internal("blur/fragment.frag").readString(); // 读取片段着色
        frameBufferA = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 576, false);
        frameBufferB = new FrameBuffer(Pixmap.Format.RGBA8888, 1024, 576, false);
        shader = new ShaderProgram(vertexShader, fragmentShader);
        blurBatch = new SpriteBatch();
        //blurBatch.setProjectionMatrix(GaussianBlur.game.camera.combined);
    }

    /**
     *     对指定舞台进行模糊处理
     *  绘制主体，注意方法处理的顺序，以及begin()跟 end()配对
     *  有些手机不支持导致shader.isCompiled() == false 无法进行着色
     *  @param stage
     */
    public static void blur(Stage stage) {

        if (shader != null && shader.isCompiled()) {
            blurBatch.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
            time += Gdx.graphics.getDeltaTime();
            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // 时间控制，显示动态变化
            blur = time < MAX_BLUR ? time : MAX_BLUR;
            blurRander(stage);
            horizontalBlur();
            verticalBlur();
        }

    }

    /**
     *  模糊主体渲染
     */
    private static void blurRander(Stage stage){

        //先执行frameBufferA捕获默认渲染之下的纹理缓存
        frameBufferA.begin();
        blurBatch.begin();
        shader.begin();
        /** 因为是循环执行，必须对参数进行重置，重新设置shader绘制需要模糊的stage部分**/
        shader.setUniformf("dir", 0f, 0f);
        shader.setUniformf("radius", radius );
        shader.setUniformf("resolution", fbo_size);
        blurBatch.setShader(shader);
        //精灵重置后绘制stage
        stage.draw();
        stage.getRoot().draw(blurBatch, 1);
        //进行刷新
        blurBatch.flush();
        //获得第一次纹理缓存
        frameBufferA.end();
    }

    /**
     *  垂直模糊渲染
     */
    private static void horizontalBlur()   {

        /**设置为垂直模糊的shader**/
        blurBatch.setShader(shader);
        shader.setUniformf("dir", xOffset, 0f);
        shader.setUniformf("radius",blur );

        frameBufferB.begin();

        blurBatch.draw(frameBufferA.getColorBufferTexture(), 0, 0);//绘制纹理缓存A
        blurBatch.flush();
        frameBufferB.end();
    }

    /**
     *  水平模糊渲染
     */
    private static void verticalBlur()   {
        /*
         * 设置为 水平模糊shader再进行绘制
         */
        shader.setUniformf("dir", 0f, yOffset);
        shader.setUniformf("radius",blur );

        blurBatch.draw(frameBufferB.getColorBufferTexture(), 0, 0);// 绘制纹理缓存B
        blurBatch.flush();
        blurBatch.end();
        shader.end();

    }

    public static void dispose() {
        blurBatch.dispose();
        shader.dispose();
    }
}
