package com.zhfy.game.framework.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class WarpUtils {

    private  String vertexShader;            // 定点
    private  String fragmentShader;            // 片段
    private  ShaderProgram shader;            // 自定义着色器
    private Mesh mesh;

    public WarpUtils(Stage stage){
        // 以下命令供GPU使用(不支持GLES2.0就不用跑了)
         vertexShader = "attribute vec4 a_position;\n"
                + "attribute vec2 a_texCoord" + "varying vec2 v_texCoord;\n"
                + "void main() " + "{ " + " gl_Position = a_position;\n"
                + " v_texCoord = a_texCoord; " + "} ";
         fragmentShader = "ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "endif \n"
                + "varying vec2 v_texCoord;\n"
                + "uniform sampler2D s_texture;\n"
                + "uniform sampler2D s_texture2;\n"
                + "void main()\n"
                + "{\n"
                + " gl_FragColor = texture2D( s_texture, v_texCoord ) * texture2D( s_texture2, v_texCoord);\n"
                + "} ";


        shader = new ShaderProgram(vertexShader, fragmentShader);
        //blurBatch = new SpriteBatch();
        mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        mesh.setVertices(new float[]
                {-0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1,
                        0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1,
                        0.5f, 0.5f, 0, 1, 1, 1, 1, 1, 0,
                        -0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0});
        mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
    }

    /**
     *     对指定舞台进行模糊处理
     *  绘制主体，注意方法处理的顺序，以及begin()跟 end()配对
     *  有些手机不支持导致shader.isCompiled() == false 无法进行着色
     *  @param stage
     */
    public  void blur(SpriteBatch batch,Stage stage) {
        if (shader != null && shader.isCompiled()) {
           // blurBatch.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
           // batch.setProjectionMatrix(stage.getCamera().combined);
            batch.setShader(shader);

            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // 开始使用ShaderProgram渲染
            shader.bind();
            shader.setUniformi("s_texture", 0);
            shader.setUniformi("s_texture2", 1);
            //mesh.render(shader, GL20.GL_TRIANGLES);
            stage.getRoot().draw(batch, 1);
            batch.flush();
        }

    }

   /* *//**
     *  模糊主体渲染
     *//*
    private  void blurRander(SpriteBatch batch,Stage stage){

        //先执行frameBufferA捕获默认渲染之下的纹理缓存
        frameBufferA.begin();
      //  batch.begin();
        shader.bind();
        *//** 因为是循环执行，必须对参数进行重置，重新设置shader绘制需要模糊的stage部分**//*
        shader.setUniformMatrix("u_projTrans",stage.getCamera().combined);
        shader.setUniformi("u_texture",0);
        shader.setUniformf("dir", 0f, 0f);
        shader.setUniformf("radius", radius );
        shader.setUniformf("resolution", fbo_size);
        batch.setShader(shader);
        //精灵重置后绘制stage
        stage.draw();
        mesh.render(shader, GL20.GL_TRIANGLES);
        //stage.getRoot().draw(batch, 1);
        //进行刷新
        batch.flush();
        //获得第一次纹理缓存
        frameBufferA.end();
    }*/



    public  void dispose() {
       // blurBatch.dispose();
        shader.dispose();
    }
}
