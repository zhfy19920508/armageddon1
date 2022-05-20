v2f o;
 o.pos = mul(UNITY_MATRIX_MVP, v.vertex);
 o.uv = TRANSFORM_TEX(v.texcoord, _MainTex);
 return o;