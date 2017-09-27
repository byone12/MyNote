package com.example.zhouzhou.mynote.cube;

import android.util.Log;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public class GLFace {

	public GLFace() {
		
	}
	
	// 三角形
	public GLFace(GLVertex v1, GLVertex v2, GLVertex v3) {
		addVertex(v1);
		addVertex(v2);
		addVertex(v3);
	}	
	// 四边形
	public GLFace(GLVertex v1, GLVertex v2, GLVertex v3, GLVertex v4) {
		addVertex(v1);
		addVertex(v2);
		addVertex(v3);
		addVertex(v4);
	}
		
	public void addVertex(GLVertex v) {
		mVertexList.add(v);
	}
	
	// 所有顶点加进来之后调用
	public void setColor(GLColor c) {
		
		int last = mVertexList.size() - 1;
		if (last < 2) {
			Log.e("GLFace", "not enough vertices in setColor()");
		} else {
			GLVertex vertex = mVertexList.get(last);
			
			// 颜色没有被设置时进行设置
			if (mColor == null) {
				while (vertex.color != null) {
					mVertexList.add(0, vertex);
					mVertexList.remove(last + 1);
					vertex = mVertexList.get(last);
				}
			}
			
			vertex.color = c;
		}

		mColor = c;
	}
	
	public int getIndexCount() {
		return (mVertexList.size() - 2) * 3;
	}
	
	public void putIndices(ShortBuffer buffer) {
		int last = mVertexList.size() - 1;

		GLVertex v0 = mVertexList.get(0);
		GLVertex vn = mVertexList.get(last);
		
		for (int i = 1; i < last; i++) {
			GLVertex v1 = mVertexList.get(i);
			buffer.put(v0.index);
			buffer.put(v1.index);
			buffer.put(vn.index);
			v0 = v1;
		}
	}
	
	private ArrayList<GLVertex> mVertexList = new ArrayList<GLVertex>();
	private GLColor mColor;
}
