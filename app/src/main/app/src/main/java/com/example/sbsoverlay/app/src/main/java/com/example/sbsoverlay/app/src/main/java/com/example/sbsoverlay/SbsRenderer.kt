package com.example.sbsoverlay

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * SBS（分屏/并排）3D 效果的 OpenGL 渲染器
 * 负责在后台服务或 SurfaceView 中进行画面渲染
 */
class SbsRenderer : GLSurfaceView.Renderer {

    // 可以在这里定义您的顶点着色器和片元着色器代码
    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = vPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}"

    private var program: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 设置清屏时背景的颜色（这里设为深灰色/黑色底）
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        
        // 初始化编译 OpenGL 着色器程序
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 当屏幕尺寸或视口发生改变时回调
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 每一帧的绘制核心逻辑
        // 清除屏幕颜色缓冲区
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        
        // 使用编译好的程序进行绘制
        GLES20.glUseProgram(program)
        
        // 提示：如果您后续需要实现 SBS 左右分屏逻辑
        // 可以通过改变 glViewport 分别绘制左眼和右眼的画面
        // 例如：
        // GLES20.glViewport(0, 0, width / 2, height) -> 绘制左半边
        // GLES20.glViewport(width / 2, 0, width / 2, height) -> 绘制右半边
    }

    /**
     * 辅助方法：用于加载和编译着色器
     */
    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
