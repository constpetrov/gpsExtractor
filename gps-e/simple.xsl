<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl">
<xsl:template match="/">
<?xml version="1.0" encoding="WINDOWS-1251"?>
 
   
   <title>����������</title>  
   <style type="text/css">  
    .block1 {   
     width: 100%; 
      
     padding: 3px;    
     border: solid 1px black;   
     float: left;  
    }  
    .block2 {   
     width: 100%;   
       
     padding: 1px;   
     border: solid 1px white;   
     float: left;   
      
    }  
   </style>   
  
<body>
<div class="block1">
<div class="block2">
  <strong><xsl:value-of select="//results/short/all/@caption"/></strong>
  <xsl:value-of select="//results/short/all/@dist"/> ����������
</div>
<div class="block2">
  <strong>�� �����</strong>
  <xsl:value-of select="//results/short/all/@time"/>
</div>
<div class="block2">

  <strong>�� ������� ���������</strong>
  <xsl:value-of select="//results/short/all/@avg"/> ���������� � ���
</div>
</div>

<div class="block1">
�� ���:
<div class="block1">
<div class="block2">
    <strong><xsl:value-of select="//results/short/onBike/@caption"/></strong>
    <xsl:value-of select="//results/short/onBike/@dist"/> ����������
    </div>
<div class="block2">
    <strong>�� �����</strong>
    <xsl:value-of select="//results/short/onBike/@time"/>
    </div>
<div class="block2">
    <strong>�� ������� ���������</strong>
    <xsl:value-of select="//results/short/onBike/@avg"/> ���������� � ���
    </div>
    </div>
<br></br>    
<div class="block1">    
<div class="block2">
    <strong><xsl:value-of select="//results/short/onFoot/@caption"/></strong>
    <xsl:value-of select="//results/short/onFoot/@dist"/> ����������
    </div>
<div class="block2">
    <strong>�� �����</strong>
    <xsl:value-of select="//results/short/onFoot/@time"/>
    </div>
<div class="block2">
    <strong>�� ������� ���������</strong>
    <xsl:value-of select="//results/short/onFoot/@avg"/> ���������� � ���
    </div>
  </div>
  </div>
 
</body>
</xsl:template>
</xsl:stylesheet>