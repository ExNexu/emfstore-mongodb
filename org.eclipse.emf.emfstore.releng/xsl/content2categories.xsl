<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		<repository name='ECP BUILD_QUALIFIER Categories'
			type='org.eclipse.equinox.internal.p2.metadata.repository.LocalMetadataRepository' version='1.0.0'>
			<properties size='2'>
				<property name='p2.timestamp' value='1307560601786'/>
				<property name='p2.compressed' value='true'/>
			</properties>
			<units size='1'>
				<unit id="ecp.BUILD_QUALIFIER" version="0.0.0">
					<properties size="2">
						<property name="org.eclipse.equinox.p2.name" value="ECP BUILD_QUALIFIER"/>
						<property name="org.eclipse.equinox.p2.type.category" value="true"/>
					</properties>
					<provides size="1">
						<provided namespace="org.eclipse.equinox.p2.iu" name="ecp.BUILD_QUALIFIER" version="0.0.0"/>
					</provides>
					<requires size="REQUIRES_SIZE">
						<xsl:apply-templates select="//property[@name='org.eclipse.equinox.p2.type.category']"/>
					</requires>
					<touchpoint id="null" version="0.0.0"/>
				</unit>
			</units>
		</repository>
	</xsl:template>
	
	<xsl:template match="property[../../@id!='Default']">
		<required namespace="org.eclipse.equinox.p2.iu">
			<xsl:attribute name="name">
				<xsl:value-of select="../../@id"/>
			</xsl:attribute>
			<xsl:attribute name="version">
				<xsl:value-of select="../../@version"/>
			</xsl:attribute>
		</required>
	</xsl:template>
	
</xsl:stylesheet>