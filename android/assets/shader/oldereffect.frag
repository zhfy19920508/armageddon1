//���������� uv.yֵ����һЩ�������ʵ�ֶ�����Ч�� _SinTime��Unity���õı��� ������ȡһ��-1��1������ֵ
 half2 mainTexUV = half2(i.uv.x, i.uv.y+(_RandomValue*_SinTime.z * 0.005));
 fixed4 mainTex = tex2D(_MainTex, mainTexUV);

 fixed4 vignetteTex = tex2D(_VignetteTex, i.uv);

 half2 scratchesUV = half2(i.uv.x + (_RandomValue * _SinTime.z * _ScratchesXSpeed),
 i.uv.y + (_RandomValue * _Time.x * _ScratchesYSpeed));
 fixed4 scratchesTex = tex2D(_ScratchesTex, scratchesUV); 

 half2 dustUV = half2(i.uv.x + (_RandomValue * _SinTime.z * _DustXSpeed),
 i.uv.y + (_Time.x * _DustYSpeed));
 fixed4 dustTex = tex2D(_DustTex, dustUV);

 //���YIQ ֵ
 fixed lum = dot(fixed3(0.299, 0.587, 0.114), mainTex.rgb);

 fixed4 finalColor = lum + lerp(_SepiaColor, _SepiaColor + fixed4(0.1f, 0.1f, 0.1f, 0.1f), _RandomValue);

 fixed3 constantWhite = fixed3(1, 1, 1);

 finalColor = lerp(finalColor, finalColor * vignetteTex, _VignetteAmount);
 finalColor.rgb *= lerp(scratchesTex, constantWhite, _RandomValue);
 finalColor.rgb *= lerp(dustTex, constantWhite, (_RandomValue * _SinTime.z));
 finalColor = lerp(mainTex, finalColor, _EffectAmount);

 return finalColor;