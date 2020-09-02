// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawShape3D : TXV_Module {
	classvar <defaultName = "TXV Shape 3D";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	// add text array to ..."shapeType/fixedValue":
	// , ["Sphere (radius)", "IcoSphere (radius)", "Box (width, height, depth)", "Plane (width, height)", "Cylinder (radius, height)", "Rounded Cylinder (radius, height, roundness)", "Uncapped Cylinder (radius, height)", "Cone (radius, height)", "Rounded Cone (radius, height, roundness)", "Uncapped Cone (radius, height)", "Diamond (parashape: width, height, depth)", "Torus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Ellipsoid (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Paraboloid (parashape: w, h, d, x1, x2, y1, y2)", "HyperbolicParaboloid (parashape: width, height, depth)", "Hyperboloid1 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Hyperboloid2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "RightConoid (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Helicoid (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Periwinkle (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Topshell (parashape: width, height, depth)", "ConeShell (parashape: width, height, depth)", "Periwinkle2 (parashape: width, height, depth)", "ToothedTopshell (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "TurretShell (parashape: width, height, depth)", "ShellX (parashape: width, height, depth)", "SpindleShell (parashape: width, height, depth)", "Topshell2 (parashape: width, height, depth)", "CrossCap (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Klein (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Klein2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Cosinus (parashape: w, h, d, z1, z2)", "Moebius (parashape: width, height, depth)", "Reiman (parashape: width, height, depth)", "Henneberg (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Enneper (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Enneper2 (parashape: w, h, d, x1, x2, y1, y2, z1)", "Helix (parashape: width, height, depth)", "Hexaedron (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Catalan (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Toupie (parashape: w, h, d, x1, x2, y1, y2)", "Toupie2 (parashape: w, h, d, x1, x2, y1, y2)", "Trumpet (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Catenoid (parashape: width, height, depth)", "Crescent (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Crescent2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "HyperbolicHelicoid (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Horn (parashape: w, h, d, x1, x2, y1, y2, z1)", "Fresnel (parashape: width, height, depth)", "Steiner (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "MaedersOwl (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "TrippleFin (parashape: width, height, depth)", "EightSurface (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "EightSurface2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Roman (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Dini (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Apple (parashape: width, height, depth)", "Drop (parashape: w, h, d, x1, x2, y1, y2, z1, z2, z3)", "Kidney (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "LimpetTorus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Lemniscape (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Folium (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Seed (parashape: width, height, depth)" , "PinchedSphere (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "PinchedPillow (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "TwistedSphere (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "TwistedTorus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Hurricane (parashape: width, height, depth)", "Star5 (parashape: width, height, depth)", ]

	// add text array to ..."distortOrder/fixedValue":
	// , ["Wave Twist Bend", "Wave Bend Twist", "Twist Bend Wave", "Twist Wave Bend", "Bend Twist Wave", "Bend Wave Twist" ]

	// add text array to ..."wave1Type/fixedValue":
	// add text array to ..."wave2Type/fixedValue":
	// add text array to ..."wave3Type/fixedValue":
	// , ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]

	// add text for "perlin1Type/fixedValue"
	// add text for "perlin2Type/fixedValue"
	// add text for "perlin3Type/fixedValue"
	// , ["Normal", "Quantised"]

	// add text array to ..." waveDistort1Waveform/fixedValue":
	// add text array to ..." waveDistort2Waveform/fixedValue":
	// add text array to ..." waveDistort3Waveform/fixedValue":
	// add text array to ..." waveDistort4Waveform/fixedValue":
	// add text array to ..." waveDistort5Waveform/fixedValue":
	// add text array to ..." waveDistort6Waveform/fixedValue":
	// , ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]

	// add text array to ..." waveDistort1Axis/fixedValue":
	// add text array to ..." waveDistort2Axis/fixedValue":
	// add text array to ..." waveDistort3Axis/fixedValue":
	// add text array to ..." waveDistort4Axis/fixedValue":
	// add text array to ..." waveDistort5Axis/fixedValue":
	// add text array to ..." waveDistort6Axis/fixedValue":
	// , ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]

	// add text array to ..." waveDistort1Type/fixedValue":
	// add text array to ..." waveDistort2Type/fixedValue":
	// add text array to ..." waveDistort3Type/fixedValue":
	// add text array to ..." waveDistort4Type/fixedValue":
	// add text array to ..." waveDistort5Type/fixedValue":
	// add text array to ..." waveDistort6Type/fixedValue":
	// , ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]

	// add text array to ..."twistAxis/fixedValue":
	// , ["X Axis", "Y Axis", "Z Axis", ]

	// add text array to ... bendType/fixedValue":
	//, ["Back In", "Back Out", "Back In & Out", "Bounce In", "Bounce Out", "Bounce In & Out", "Circ In", "Circ Out", "Circ In & Out", "Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawShape3D_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/drawActive", [
			["bool_int", "/TXDrawShape3D_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawShape3D_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/drawActive/useExtMod", 0],
			["float", "/TXDrawShape3D_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawShape3D_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawShape3D_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXDrawShape3D_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawShape3D_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDrawShape3D_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXDrawShape3D_1/useImageAsTexture", 0],
		["bool_int", "/TXDrawShape3D_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/shapeType", [
			["int", "/TXDrawShape3D_1/shapeType/fixedValue", 0, 0, 68, ["Sphere (radius)", "IcoSphere (radius)", "Box (width, height, depth)", "Plane (width, height)", "Cylinder (radius, height)", "Rounded Cylinder (radius, height, roundness)", "Uncapped Cylinder (radius, height)", "Cone (radius, height)", "Rounded Cone (radius, height, roundness)", "Uncapped Cone (radius, height)", "Diamond (parashape: width, height, depth)", "Torus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Ellipsoid (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Paraboloid (parashape: w, h, d, x1, x2, y1, y2)", "HyperbolicParaboloid (parashape: width, height, depth)", "Hyperboloid1 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Hyperboloid2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "RightConoid (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Helicoid (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Periwinkle (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Topshell (parashape: width, height, depth)", "ConeShell (parashape: width, height, depth)", "Periwinkle2 (parashape: width, height, depth)", "ToothedTopshell (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "TurretShell (parashape: width, height, depth)", "ShellX (parashape: width, height, depth)", "SpindleShell (parashape: width, height, depth)", "Topshell2 (parashape: width, height, depth)", "CrossCap (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Klein (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Klein2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Cosinus (parashape: w, h, d, z1, z2)", "Moebius (parashape: width, height, depth)", "Reiman (parashape: width, height, depth)", "Henneberg (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Enneper (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Enneper2 (parashape: w, h, d, x1, x2, y1, y2, z1)", "Helix (parashape: width, height, depth)", "Hexaedron (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Catalan (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Toupie (parashape: w, h, d, x1, x2, y1, y2)", "Toupie2 (parashape: w, h, d, x1, x2, y1, y2)", "Trumpet (parashape: w, h, d, x1, x2, y1, y2, z1, z2)", "Catenoid (parashape: width, height, depth)", "Crescent (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Crescent2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "HyperbolicHelicoid (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Horn (parashape: w, h, d, x1, x2, y1, y2, z1)", "Fresnel (parashape: width, height, depth)", "Steiner (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "MaedersOwl (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "TrippleFin (parashape: width, height, depth)", "EightSurface (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "EightSurface2 (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Roman (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Dini (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Apple (parashape: width, height, depth)", "Drop (parashape: w, h, d, x1, x2, y1, y2, z1, z2, z3)", "Kidney (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "LimpetTorus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "Lemniscape (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Folium (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Seed (parashape: width, height, depth)" , "PinchedSphere (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "PinchedPillow (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "TwistedSphere (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2)", "TwistedTorus (parashape: w, h, d, x1, x2, x3, y1, y2, y3, z1, z2, z3)", "Hurricane (parashape: width, height, depth)", "Star5 (parashape: width, height, depth)", ]],
			["float", "/TXDrawShape3D_1/shapeType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/shapeType/useExtMod", 0],
			["float", "/TXDrawShape3D_1/shapeType/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/shapeType/softMin", 0, 0, 68],
			["int", "/TXDrawShape3D_1/shapeType/softMax", 68, 0, 68],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/fillShape", [
			["bool_int", "/TXDrawShape3D_1/fillShape/fixedValue", 1],
			["bool_int", "/TXDrawShape3D_1/fillShape/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/fillShape/useExtMod", 0],
			["float", "/TXDrawShape3D_1/fillShape/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/distortOrder", [
			["int", "/TXDrawShape3D_1/distortOrder/fixedValue", 0, 0, 5, ["Wave Twist Bend", "Wave Bend Twist", "Twist Bend Wave", "Twist Wave Bend", "Bend Twist Wave", "Bend Wave Twist" ]],
			["float", "/TXDrawShape3D_1/distortOrder/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/distortOrder/useExtMod", 0],
			["float", "/TXDrawShape3D_1/distortOrder/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/distortOrder/softMin", 0, 0, 5],
			["int", "/TXDrawShape3D_1/distortOrder/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/width", [
			["float", "/TXDrawShape3D_1/width/fixedValue", 0.05, 0, 1],
			["float", "/TXDrawShape3D_1/width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/width/useExtMod", 0],
			["float", "/TXDrawShape3D_1/width/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/width/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/width/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/height", [
			["float", "/TXDrawShape3D_1/height/fixedValue", 0.1, 0, 1],
			["float", "/TXDrawShape3D_1/height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/height/useExtMod", 0],
			["float", "/TXDrawShape3D_1/height/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/height/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/height/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawShape3D_1/useMaxWidthToScaleHeight", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/depth", [
			["float", "/TXDrawShape3D_1/depth/fixedValue", 0.05, 0, 1],
			["float", "/TXDrawShape3D_1/depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/depth/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/depth/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawShape3D_1/useMaxWidthToScaleDepth", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/radius", [
			["float", "/TXDrawShape3D_1/radius/fixedValue", 0.05, 0, 1],
			["float", "/TXDrawShape3D_1/radius/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/radius/useExtMod", 0],
			["float", "/TXDrawShape3D_1/radius/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/radius/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/radius/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/roundness", [
			["float", "/TXDrawShape3D_1/roundness/fixedValue", 0.2, 0, 1],
			["float", "/TXDrawShape3D_1/roundness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/roundness/useExtMod", 0],
			["float", "/TXDrawShape3D_1/roundness/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/roundness/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/roundness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionWidth", [
			["int", "/TXDrawShape3D_1/resolutionWidth/fixedValue", 24, 2, 128],
			["float", "/TXDrawShape3D_1/resolutionWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionWidth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionWidth/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionWidth/softMin", 2, 2, 1024],
			["int", "/TXDrawShape3D_1/resolutionWidth/softMax", 128, 2, 1024],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionHeight", [
			["int", "/TXDrawShape3D_1/resolutionHeight/fixedValue", 24, 2, 128],
			["float", "/TXDrawShape3D_1/resolutionHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionHeight/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionHeight/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionHeight/softMin", 2, 2, 1024],
			["int", "/TXDrawShape3D_1/resolutionHeight/softMax", 128, 2, 1024],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionDepth", [
			["int", "/TXDrawShape3D_1/resolutionDepth/fixedValue", 24, 2, 128],
			["float", "/TXDrawShape3D_1/resolutionDepth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionDepth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionDepth/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionDepth/softMin", 2, 2, 1024],
			["int", "/TXDrawShape3D_1/resolutionDepth/softMax", 128, 2, 1024],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionRadius", [
			["int", "/TXDrawShape3D_1/resolutionRadius/fixedValue", 24, 2, 128],
			["float", "/TXDrawShape3D_1/resolutionRadius/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionRadius/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionRadius/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionRadius/softMin", 2, 2, 1024],
			["int", "/TXDrawShape3D_1/resolutionRadius/softMax", 128, 2, 1024],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionIsoSphere", [
			["int", "/TXDrawShape3D_1/resolutionIsoSphere/fixedValue", 3, 1, 5],
			["float", "/TXDrawShape3D_1/resolutionIsoSphere/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionIsoSphere/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionIsoSphere/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionIsoSphere/softMin", 1, 1, 5],
			["int", "/TXDrawShape3D_1/resolutionIsoSphere/softMax", 5, 1, 5],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/resolutionCap", [
			["int", "/TXDrawShape3D_1/resolutionCap/fixedValue", 12, 2, 128],
			["float", "/TXDrawShape3D_1/resolutionCap/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/resolutionCap/useExtMod", 0],
			["float", "/TXDrawShape3D_1/resolutionCap/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/resolutionCap/softMin", 2, 2, 1024],
			["int", "/TXDrawShape3D_1/resolutionCap/softMax", 128, 2, 1024],
		]],
		["int", "/TXDrawShape3D_1/resolutionParaShape", 48, 2, 256],
		["bool_int", "/TXDrawShape3D_1/smoothDistortedParaShape", 1],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/x1", [
			["float", "/TXDrawShape3D_1/x1/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/x1/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/x1/useExtMod", 0],
			["float", "/TXDrawShape3D_1/x1/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/x1/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/x1/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/x1Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/x2", [
			["float", "/TXDrawShape3D_1/x2/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/x2/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/x2/useExtMod", 0],
			["float", "/TXDrawShape3D_1/x2/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/x2/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/x2/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/x2Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/x3", [
			["float", "/TXDrawShape3D_1/x3/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/x3/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/x3/useExtMod", 0],
			["float", "/TXDrawShape3D_1/x3/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/x3/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/x3/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/x3Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/y1", [
			["float", "/TXDrawShape3D_1/y1/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/y1/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/y1/useExtMod", 0],
			["float", "/TXDrawShape3D_1/y1/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/y1/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/y1/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/y1Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/y2", [
			["float", "/TXDrawShape3D_1/y2/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/y2/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/y2/useExtMod", 0],
			["float", "/TXDrawShape3D_1/y2/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/y2/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/y2/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/y2Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/y3", [
			["float", "/TXDrawShape3D_1/y3/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/y3/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/y3/useExtMod", 0],
			["float", "/TXDrawShape3D_1/y3/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/y3/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/y3/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/y3Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/z1", [
			["float", "/TXDrawShape3D_1/z1/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/z1/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/z1/useExtMod", 0],
			["float", "/TXDrawShape3D_1/z1/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/z1/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/z1/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/z1Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/z2", [
			["float", "/TXDrawShape3D_1/z2/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/z2/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/z2/useExtMod", 0],
			["float", "/TXDrawShape3D_1/z2/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/z2/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/z2/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/z2Quantize", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/z3", [
			["float", "/TXDrawShape3D_1/z3/fixedValue", 1, -2, 2],
			["float", "/TXDrawShape3D_1/z3/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/z3/useExtMod", 0],
			["float", "/TXDrawShape3D_1/z3/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/z3/softMin", -2, -20, 20],
			["float", "/TXDrawShape3D_1/z3/softMax", 2, -20, 20],
		]],
		["bool_int", "/TXDrawShape3D_1/z3Quantize", 0],
		["bool_int", "/TXDrawShape3D_1/useXValsForY", 0],
		["bool_int", "/TXDrawShape3D_1/useXValsForZ", 0],
		["bool_int", "/TXDrawShape3D_1/ignoreAllXYZVals", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/positionX", [
			["float", "/TXDrawShape3D_1/positionX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/positionX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/positionX/softMin", 0, -20, 20],
			["float", "/TXDrawShape3D_1/positionX/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/positionY", [
			["float", "/TXDrawShape3D_1/positionY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/positionY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/positionY/softMin", 0, -20, 20],
			["float", "/TXDrawShape3D_1/positionY/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/positionZ", [
			["float", "/TXDrawShape3D_1/positionZ/fixedValue", 0.0, 0, 1],
			["float", "/TXDrawShape3D_1/positionZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/positionZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/positionZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/positionZ/softMin", -1, -20, 20],
			["float", "/TXDrawShape3D_1/positionZ/softMax", 0, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/rotateX", [
			["float", "/TXDrawShape3D_1/rotateX/fixedValue", 0, -360, 360],
			["float", "/TXDrawShape3D_1/rotateX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/rotateX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/rotateX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/rotateX/softMin", -360, -360, 360],
			["float", "/TXDrawShape3D_1/rotateX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/rotateY", [
			["float", "/TXDrawShape3D_1/rotateY/fixedValue", 0, -360, 360],
			["float", "/TXDrawShape3D_1/rotateY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/rotateY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/rotateY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/rotateY/softMin", -360, -360, 360],
			["float", "/TXDrawShape3D_1/rotateY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/rotateZ", [
			["float", "/TXDrawShape3D_1/rotateZ/fixedValue", 0, -360, 360],
			["float", "/TXDrawShape3D_1/rotateZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/rotateZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/rotateZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/rotateZ/softMin", -360, -360, 360],
			["float", "/TXDrawShape3D_1/rotateZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/rotateMultiply", [
			["float", "/TXDrawShape3D_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawShape3D_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawShape3D_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/anchorX", [
			["float", "/TXDrawShape3D_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/anchorX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/anchorY", [
			["float", "/TXDrawShape3D_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/anchorY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/anchorZ", [
			["float", "/TXDrawShape3D_1/anchorZ/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/anchorZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorZ/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/anchorZ/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/scaleX", [
			["float", "/TXDrawShape3D_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawShape3D_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/scaleX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawShape3D_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/scaleY", [
			["float", "/TXDrawShape3D_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawShape3D_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/scaleY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawShape3D_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/scaleZ", [
			["float", "/TXDrawShape3D_1/scaleZ/fixedValue", 1, 0, 3],
			["float", "/TXDrawShape3D_1/scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/scaleZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/scaleZ/softMin", 0, -10, 10],
			["float", "/TXDrawShape3D_1/scaleZ/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawShape3D_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDrawShape3D_1/useScaleXForScaleZ", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialShininess", [
			["float", "/TXDrawShape3D_1/materialShininess/fixedValue", 25, 0, 128],
			["float", "/TXDrawShape3D_1/materialShininess/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialShininess/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialShininess/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialShininess/softMin", 0, 0, 128],
			["float", "/TXDrawShape3D_1/materialShininess/softMax", 128, 0, 128],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialDiffuseColorHue", [
			["float", "/TXDrawShape3D_1/materialDiffuseColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorHue/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialDiffuseColorHueRotate", [
			["float", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialDiffuseColorSaturation", [
			["float", "/TXDrawShape3D_1/materialDiffuseColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorSaturation/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialDiffuseColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialDiffuseColorBrightness", [
			["float", "/TXDrawShape3D_1/materialDiffuseColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorBrightness/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialDiffuseColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialDiffuseColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/materialDiffuseColorHSBAsRGB", [
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/materialDiffuseColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialDiffuseColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXDrawShape3D_1/useDiffuseColorForAmbient", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialAmbientColorHue", [
			["float", "/TXDrawShape3D_1/materialAmbientColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorHue/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialAmbientColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialAmbientColorHueRotate", [
			["float", "/TXDrawShape3D_1/materialAmbientColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorHueRotate/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialAmbientColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialAmbientColorSaturation", [
			["float", "/TXDrawShape3D_1/materialAmbientColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorSaturation/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialAmbientColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialAmbientColorBrightness", [
			["float", "/TXDrawShape3D_1/materialAmbientColorBrightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorBrightness/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialAmbientColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialAmbientColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/materialAmbientColorHSBAsRGB", [
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/materialAmbientColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialAmbientColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXDrawShape3D_1/useDiffuseColorForSpecular", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialSpecularColorHue", [
			["float", "/TXDrawShape3D_1/materialSpecularColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorHue/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialSpecularColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialSpecularColorHueRotate", [
			["float", "/TXDrawShape3D_1/materialSpecularColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorHueRotate/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialSpecularColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialSpecularColorSaturation", [
			["float", "/TXDrawShape3D_1/materialSpecularColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorSaturation/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialSpecularColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialSpecularColorBrightness", [
			["float", "/TXDrawShape3D_1/materialSpecularColorBrightness/fixedValue", 1, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorBrightness/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialSpecularColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialSpecularColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/materialSpecularColorHSBAsRGB", [
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/materialSpecularColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialSpecularColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXDrawShape3D_1/useDiffuseColorForEmissive", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialEmissiveColorHue", [
			["float", "/TXDrawShape3D_1/materialEmissiveColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorHue/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHue/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHue/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialEmissiveColorHueRotate", [
			["float", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialEmissiveColorSaturation", [
			["float", "/TXDrawShape3D_1/materialEmissiveColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorSaturation/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialEmissiveColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorSaturation/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/materialEmissiveColorBrightness", [
			["float", "/TXDrawShape3D_1/materialEmissiveColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorBrightness/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialEmissiveColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorBrightness/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/materialEmissiveColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/materialEmissiveColorHSBAsRGB", [
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/materialEmissiveColorHSBAsRGB/useExtMod", 0],
			["float", "/TXDrawShape3D_1/materialEmissiveColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/waveDistortActive", [
			["bool_int", "/TXDrawShape3D_1/waveDistortActive/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/waveDistortActive/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/waveDistortActive/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistortActive/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/wave1Type", [
			["int", "/TXDrawShape3D_1/wave1Type/fixedValue", 0, 0, 7, ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawShape3D_1/wave1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave1Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave1Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/wave1Type/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/wave1Type/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave1Phase", [
			["float", "/TXDrawShape3D_1/wave1Phase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave1Phase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave1Phase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1Phase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1Phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave1Freq", [
			["float", "/TXDrawShape3D_1/wave1Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/wave1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave1Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave1Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/wave1Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/wave2Type", [
			["int", "/TXDrawShape3D_1/wave2Type/fixedValue", 0, 0, 7, ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawShape3D_1/wave2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave2Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave2Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/wave2Type/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/wave2Type/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave2Phase", [
			["float", "/TXDrawShape3D_1/wave2Phase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave2Phase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave2Phase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2Phase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2Phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave2Freq", [
			["float", "/TXDrawShape3D_1/wave2Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/wave2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave2Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave2Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/wave2Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/wave3Type", [
			["int", "/TXDrawShape3D_1/wave3Type/fixedValue", 0, 0, 7, ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawShape3D_1/wave3Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave3Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave3Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/wave3Type/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/wave3Type/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave3Phase", [
			["float", "/TXDrawShape3D_1/wave3Phase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave3Phase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave3Phase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3Phase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3Phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave3Freq", [
			["float", "/TXDrawShape3D_1/wave3Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/wave3Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave3Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave3Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/wave3Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/wave4Type", [
			["int", "/TXDrawShape3D_1/wave4Type/fixedValue", 0, 0, 7, ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawShape3D_1/wave4Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave4Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave4Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/wave4Type/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/wave4Type/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave4Phase", [
			["float", "/TXDrawShape3D_1/wave4Phase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave4Phase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave4Phase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4Phase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4Phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave4Freq", [
			["float", "/TXDrawShape3D_1/wave4Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/wave4Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave4Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave4Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/wave4Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/wave5Type", [
			["int", "/TXDrawShape3D_1/wave5Type/fixedValue", 0, 0, 7, ["Sine", "Cosine", "Square", "RampUp", "RampDown", "Triangle", "Custom Curve", "Custom Curve - Mirrored"]],
			["float", "/TXDrawShape3D_1/wave5Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave5Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave5Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/wave5Type/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/wave5Type/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave5Phase", [
			["float", "/TXDrawShape3D_1/wave5Phase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5Phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave5Phase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave5Phase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5Phase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5Phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave5Freq", [
			["float", "/TXDrawShape3D_1/wave5Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/wave5Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave5Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave5Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/wave5Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/perlin1Type", [
			["int", "/TXDrawShape3D_1/perlin1Type/fixedValue", 0, 0, 1, ["Normal", "Quantised"]],
			["float", "/TXDrawShape3D_1/perlin1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin1Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin1Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin1Type/softMin", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin1Type/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin1Offset", [
			["float", "/TXDrawShape3D_1/perlin1Offset/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin1Offset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin1Offset/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin1Offset/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin1Offset/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/perlin1Offset/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin1Freq", [
			["float", "/TXDrawShape3D_1/perlin1Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/perlin1Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin1Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin1Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin1Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/perlin1Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/perlin2Type", [
			["int", "/TXDrawShape3D_1/perlin2Type/fixedValue", 0, 0, 1, ["Normal", "Quantised"]],
			["float", "/TXDrawShape3D_1/perlin2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin2Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin2Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin2Type/softMin", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin2Type/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin2Offset", [
			["float", "/TXDrawShape3D_1/perlin2Offset/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin2Offset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin2Offset/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin2Offset/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin2Offset/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/perlin2Offset/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin2Freq", [
			["float", "/TXDrawShape3D_1/perlin2Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/perlin2Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin2Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin2Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin2Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/perlin2Freq/softMax", 20, 0, 100],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/perlin3Type", [
			["int", "/TXDrawShape3D_1/perlin3Type/fixedValue", 0, 0, 1, ["Normal", "Quantised"]],
			["float", "/TXDrawShape3D_1/perlin3Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin3Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin3Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin3Type/softMin", 0, 0, 1],
			["int", "/TXDrawShape3D_1/perlin3Type/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin3Offset", [
			["float", "/TXDrawShape3D_1/perlin3Offset/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin3Offset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin3Offset/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin3Offset/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin3Offset/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/perlin3Offset/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/perlin3Freq", [
			["float", "/TXDrawShape3D_1/perlin3Freq/fixedValue", 1, 0, 20],
			["float", "/TXDrawShape3D_1/perlin3Freq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/perlin3Freq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/perlin3Freq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/perlin3Freq/softMin", 0, 0, 100],
			["float", "/TXDrawShape3D_1/perlin3Freq/softMax", 20, 0, 100],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort1On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort1Depth", [
			["float", "/TXDrawShape3D_1/waveDistort1Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort1Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort1Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort1Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort1Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort1Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort1Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort1Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort1Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort1Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort1Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort1Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort1Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort1Axis", [
			["int", "/TXDrawShape3D_1/waveDistort1Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort1Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort1Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort1Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort1Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort1Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort1Type", [
			["int", "/TXDrawShape3D_1/waveDistort1Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort1Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort1Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort1Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort1Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort1Type/softMax", 6, 0, 6],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort2On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort2Depth", [
			["float", "/TXDrawShape3D_1/waveDistort2Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort2Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort2Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort2Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort2Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort2Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort2Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort2Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort2Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort2Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort2Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort2Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort2Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort2Axis", [
			["int", "/TXDrawShape3D_1/waveDistort2Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort2Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort2Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort2Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort2Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort2Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort2Type", [
			["int", "/TXDrawShape3D_1/waveDistort2Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort2Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort2Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort2Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort2Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort2Type/softMax", 6, 0, 6],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort3On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort3Depth", [
			["float", "/TXDrawShape3D_1/waveDistort3Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort3Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort3Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort3Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort3Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort3Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort3Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort3Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort3Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort3Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort3Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort3Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort3Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort3Axis", [
			["int", "/TXDrawShape3D_1/waveDistort3Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort3Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort3Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort3Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort3Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort3Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort3Type", [
			["int", "/TXDrawShape3D_1/waveDistort3Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort3Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort3Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort3Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort3Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort3Type/softMax", 6, 0, 6],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort4On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort4Depth", [
			["float", "/TXDrawShape3D_1/waveDistort4Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort4Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort4Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort4Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort4Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort4Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort4Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort4Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort4Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort4Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort4Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort4Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort4Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort4Axis", [
			["int", "/TXDrawShape3D_1/waveDistort4Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort4Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort4Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort4Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort4Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort4Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort4Type", [
			["int", "/TXDrawShape3D_1/waveDistort4Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort4Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort4Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort4Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort4Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort4Type/softMax", 6, 0, 6],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort5On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort5Depth", [
			["float", "/TXDrawShape3D_1/waveDistort5Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort5Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort5Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort5Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort5Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort5Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort5Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort5Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort5Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort5Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort5Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort5Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort5Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort5Axis", [
			["int", "/TXDrawShape3D_1/waveDistort5Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort5Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort5Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort5Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort5Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort5Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort5Type", [
			["int", "/TXDrawShape3D_1/waveDistort5Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort5Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort5Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort5Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort5Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort5Type/softMax", 6, 0, 6],
		]],
		["bool_int", "/TXDrawShape3D_1/waveDistort6On", 0],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/waveDistort6Depth", [
			["float", "/TXDrawShape3D_1/waveDistort6Depth/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort6Depth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort6Depth/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort6Depth/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/waveDistort6Depth/softMin", 0, 0, 10],
			["float", "/TXDrawShape3D_1/waveDistort6Depth/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort6Waveform", [
			["int", "/TXDrawShape3D_1/waveDistort6Waveform/fixedValue", 0, 0, 7, ["Wave 1", "Wave 2", "Wave 3", "Wave 4", "Wave 5", "Perlin 1", "Perlin 2", "Perlin 3", ]],
			["float", "/TXDrawShape3D_1/waveDistort6Waveform/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort6Waveform/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort6Waveform/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort6Waveform/softMin", 0, 0, 7],
			["int", "/TXDrawShape3D_1/waveDistort6Waveform/softMax", 7, 0, 7],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort6Axis", [
			["int", "/TXDrawShape3D_1/waveDistort6Axis/fixedValue", 0, 0, 8, ["Pos X on Y Axis", "Pos X on Z Axis", "Pos Y on X Axis", "Pos Y on Z Axis", "Pos Z on X Axis", "Pos Z on Y Axis", "Pos Y & Z Radial on X Axis",  "Pos X & Z Radial on Y Axis",  "Pos X & Y Radial on Z Axis", ]],
			["float", "/TXDrawShape3D_1/waveDistort6Axis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort6Axis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort6Axis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort6Axis/softMin", 0, 0, 8],
			["int", "/TXDrawShape3D_1/waveDistort6Axis/softMax", 8, 0, 8],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/waveDistort6Type", [
			["int", "/TXDrawShape3D_1/waveDistort6Type/fixedValue", 0, 0, 6, ["Multiply all values", "Multiply positive values only", "Multiply negative values only", "Add all values", "Add positive, subtract negative values", "Add positive values only", "Subtract negative values only", ]],
			["float", "/TXDrawShape3D_1/waveDistort6Type/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/waveDistort6Type/useExtMod", 0],
			["float", "/TXDrawShape3D_1/waveDistort6Type/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/waveDistort6Type/softMin", 0, 0, 6],
			["int", "/TXDrawShape3D_1/waveDistort6Type/softMax", 6, 0, 6],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/focusPointX", [
			["float", "/TXDrawShape3D_1/focusPointX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/focusPointX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/focusPointX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointX/softMin", 0, -20, 20],
			["float", "/TXDrawShape3D_1/focusPointX/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/focusPointY", [
			["float", "/TXDrawShape3D_1/focusPointY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/focusPointY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/focusPointY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointY/softMin", 0, -20, 20],
			["float", "/TXDrawShape3D_1/focusPointY/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/focusPointZ", [
			["float", "/TXDrawShape3D_1/focusPointZ/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/focusPointZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/focusPointZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/focusPointZ/softMin", 0, -20, 20],
			["float", "/TXDrawShape3D_1/focusPointZ/softMax", 1, -20, 20],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/twistActive", [
			["bool_int", "/TXDrawShape3D_1/twistActive/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/twistActive/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/twistActive/useExtMod", 0],
			["float", "/TXDrawShape3D_1/twistActive/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/twistPhase", [
			["float", "/TXDrawShape3D_1/twistPhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/twistPhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/twistPhase/useExtMod", 0],
			["float", "/TXDrawShape3D_1/twistPhase/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/twistPhase/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/twistPhase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/twistAxis", [
			["int", "/TXDrawShape3D_1/twistAxis/fixedValue", 0, 0, 2, ["X Axis", "Y Axis", "Z Axis", ]],
			["float", "/TXDrawShape3D_1/twistAxis/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/twistAxis/useExtMod", 0],
			["float", "/TXDrawShape3D_1/twistAxis/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/twistAxis/softMin", 0, 0, 2],
			["int", "/TXDrawShape3D_1/twistAxis/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/twistFreq", [
			["float", "/TXDrawShape3D_1/twistFreq/fixedValue", 0, -3, 3],
			["float", "/TXDrawShape3D_1/twistFreq/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/twistFreq/useExtMod", 0],
			["float", "/TXDrawShape3D_1/twistFreq/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/twistFreq/softMin", -3, -30, 30],
			["float", "/TXDrawShape3D_1/twistFreq/softMax", 3, -30, 30],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/twistMiddleReverse", [
			["bool_int", "/TXDrawShape3D_1/twistMiddleReverse/fixedValue", 1],
			["bool_int", "/TXDrawShape3D_1/twistMiddleReverse/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/twistMiddleReverse/useExtMod", 0],
			["float", "/TXDrawShape3D_1/twistMiddleReverse/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/useCentroidForTwistFocusPoint", [
			["bool_int", "/TXDrawShape3D_1/useCentroidForTwistFocusPoint/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/useCentroidForTwistFocusPoint/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/useCentroidForTwistFocusPoint/useExtMod", 0],
			["float", "/TXDrawShape3D_1/useCentroidForTwistFocusPoint/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/bendActive", [
			["bool_int", "/TXDrawShape3D_1/bendActive/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/bendActive/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/bendActive/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendActive/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/bendRadius", [
			["float", "/TXDrawShape3D_1/bendRadius/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawShape3D_1/bendRadius/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/bendRadius/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendRadius/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendRadius/softMin", 0, 0, 20],
			["float", "/TXDrawShape3D_1/bendRadius/softMax", 1, 0, 20],
		]],
		[ "modParameterGroupInt", "/TXDrawShape3D_1/bendType", [
			["int", "/TXDrawShape3D_1/bendType/fixedValue", 20, 0, 32, ["Back In", "Back Out", "Back In & Out", "Bounce In", "Bounce Out", "Bounce In & Out", "Circ In", "Circ Out", "Circ In & Out", "Cubic In", "Cubic Out", "Cubic In & Out", "Elastic In", "Elastic Out", "Elastic In & Out", "Exponential In", "Exponential Out", "Exponential In & Out", "Linear In (default)", "Linear Out", "Linear In & Out", "Quadratic In", "Quadratic Out", "Quadratic In & Out", "Quartic In", "Quartic Out", "Quartic In & Out", "Quintic In", "Quintic Out", "Quintic In & Out", "Sine In", "Sine Out", "Sine In & Out"]],
			["float", "/TXDrawShape3D_1/bendType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/bendType/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendType/extModValue", 0, 0, 1],
			["int", "/TXDrawShape3D_1/bendType/softMin", 0, 0, 32],
			["int", "/TXDrawShape3D_1/bendType/softMax", 32, 0, 32],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/bendInflationEffect", [
			["bool_int", "/TXDrawShape3D_1/bendInflationEffect/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/bendInflationEffect/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/bendInflationEffect/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendInflationEffect/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/bendAmountX", [
			["float", "/TXDrawShape3D_1/bendAmountX/fixedValue", 1, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/bendAmountX/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendAmountX/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountX/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/bendAmountY", [
			["float", "/TXDrawShape3D_1/bendAmountY/fixedValue", 1, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/bendAmountY/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendAmountY/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountY/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/bendAmountZ", [
			["float", "/TXDrawShape3D_1/bendAmountZ/fixedValue", 1, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/bendAmountZ/useExtMod", 0],
			["float", "/TXDrawShape3D_1/bendAmountZ/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountZ/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/bendAmountZ/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawShape3D_1/useCentroidForBendFocusPoint", [
			["bool_int", "/TXDrawShape3D_1/useCentroidForBendFocusPoint/fixedValue", 0],
			["bool_int", "/TXDrawShape3D_1/useCentroidForBendFocusPoint/fixedModMix", 0],
			["bool_int", "/TXDrawShape3D_1/useCentroidForBendFocusPoint/useExtMod", 0],
			["float", "/TXDrawShape3D_1/useCentroidForBendFocusPoint/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDrawShape3D_1/wave1CustomCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawShape3D_1/wave1CustomCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave1CustomCurveMorph", [
			["float", "/TXDrawShape3D_1/wave1CustomCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1CustomCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave1CustomCurveMorph/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave1CustomCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1CustomCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave1CustomCurveMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawShape3D_1/wave2CustomCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawShape3D_1/wave2CustomCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave2CustomCurveMorph", [
			["float", "/TXDrawShape3D_1/wave2CustomCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2CustomCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave2CustomCurveMorph/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave2CustomCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2CustomCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave2CustomCurveMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawShape3D_1/wave3CustomCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawShape3D_1/wave3CustomCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave3CustomCurveMorph", [
			["float", "/TXDrawShape3D_1/wave3CustomCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3CustomCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave3CustomCurveMorph/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave3CustomCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3CustomCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave3CustomCurveMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawShape3D_1/wave4CustomCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawShape3D_1/wave4CustomCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave4CustomCurveMorph", [
			["float", "/TXDrawShape3D_1/wave4CustomCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4CustomCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave4CustomCurveMorph/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave4CustomCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4CustomCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave4CustomCurveMorph/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawShape3D_1/wave5CustomCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawShape3D_1/wave5CustomCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawShape3D_1/wave5CustomCurveMorph", [
			["float", "/TXDrawShape3D_1/wave5CustomCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5CustomCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawShape3D_1/wave5CustomCurveMorph/useExtMod", 0],
			["float", "/TXDrawShape3D_1/wave5CustomCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5CustomCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawShape3D_1/wave5CustomCurveMorph/softMax", 1, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawShape3D_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		var typeNames = ["shapeType", "fillShape", "distortOrder", "smoothDistortedParaShape", ];
		var sizeNames = ["width", "height","useMaxWidthToScaleHeight", "depth", "useMaxWidthToScaleDepth", "radius", "roundness", ];
		var resolutionNames = ["resolutionWidth", "resolutionHeight", "resolutionDepth", "resolutionRadius",
			"resolutionIsoSphere", "resolutionCap", "resolutionParaShape", ];
		var paraShapeNames = ["x1", "x1Quantize", "x2", "x2Quantize", "x3", "x3Quantize",
			"y1", "y1Quantize", "y2", "y2Quantize", "y3", "y3Quantize", "z1", "z1Quantize", "z2", "z2Quantize",
			"z3", "z3Quantize", "useXValsForY", "useXValsForZ", "ignoreAllXYZVals",];
		var matImageNames = ["sourceImageAsset", "extSourceImageModule",
			"materialShininess", "useImageAsTexture", "useExternalSourceImage",];
		var diffuseColorNames = ["materialDiffuseColorHue", "materialDiffuseColorHueRotate",
			"materialDiffuseColorSaturation", "materialDiffuseColorBrightness", "materialDiffuseColorHSBAsRGB", ];
		var ambientColorNames = ["useDiffuseColorForAmbient", "materialAmbientColorHue",
			"materialAmbientColorHueRotate", "materialAmbientColorSaturation", "materialAmbientColorBrightness",
			"materialAmbientColorHSBAsRGB", ];
		var specularColorNames = ["useDiffuseColorForSpecular", "materialSpecularColorHue",
			"materialSpecularColorHueRotate", "materialSpecularColorSaturation", "materialSpecularColorBrightness",
			"materialSpecularColorHSBAsRGB", ];
		var emissiveColorNames = ["useDiffuseColorForEmissive", "materialEmissiveColorHue",
			"materialEmissiveColorHueRotate", "materialEmissiveColorSaturation", "materialEmissiveColorBrightness",
			"materialEmissiveColorHSBAsRGB", ];
		^[
			// format: ["Example Name", nameValidityFunction],
			["Shape$Type", {arg argName; typeNames.indexOfEqual(argName).notNil;}],
			["Shape$Size", {arg argName; sizeNames.indexOfEqual(argName).notNil;}],
			["Shape$Resolution", {arg argName; resolutionNames.indexOfEqual(argName).notNil;}],
			["Shape$Parashape XYZ", {arg argName; paraShapeNames.indexOfEqual(argName).notNil;}],

			["Material$Image & Shininess", {arg argName; matImageNames.indexOfEqual(argName).notNil;}],
			["Material$Diffuse Color", {arg argName; diffuseColorNames.indexOfEqual(argName).notNil;}],
			["Material$Ambient Color", {arg argName; ambientColorNames.indexOfEqual(argName).notNil;}],
			["Material$Specular Color", {arg argName; specularColorNames.indexOfEqual(argName).notNil;}],
			["Material$Emissive Color", {arg argName; emissiveColorNames.indexOfEqual(argName).notNil;}],

			["Wave Distort$Active & Distort 1", {arg parameterName;
				parameterName.containsi("waveDistortActive")
				|| parameterName.containsi("waveDistort1")
				;}],
			["Wave Distort$Distort 2", {arg parameterName; parameterName.containsi("waveDistort2");}],
			["Wave Distort$Distort 3", {arg parameterName; parameterName.containsi("waveDistort3");}],
			["Wave Distort$Distort 4", {arg parameterName; parameterName.containsi("waveDistort4");}],
			["Wave Distort$Distort 5", {arg parameterName; parameterName.containsi("waveDistort5");}],
			["Wave Distort$Distort 6", {arg parameterName; parameterName.containsi("waveDistort6");}],
			["Waves & Perlins$Wave 1", {arg parameterName; parameterName.containsi("wave1");}],
			["Waves & Perlins$Wave 2", {arg parameterName; parameterName.containsi("wave2");}],
			["Waves & Perlins$Wave 3", {arg parameterName; parameterName.containsi("wave3");}],
			["Waves & Perlins$Wave 4", {arg parameterName; parameterName.containsi("wave4");}],
			["Waves & Perlins$Wave 5", {arg parameterName; parameterName.containsi("wave5");}],
			["Waves & Perlins$Perlin 1", {arg parameterName; parameterName.containsi("perlin1");}],
			["Waves & Perlins$Perlin 2", {arg parameterName; parameterName.containsi("perlin2");}],
			["Waves & Perlins$Perlin 3", {arg parameterName; parameterName.containsi("perlin3");}],

			["Twist & Bend$Focus Point", {arg parameterName; parameterName.containsi("focusPoint");}],
			["Twist & Bend$Twist", {arg parameterName; parameterName.containsi("twist");}],
			["Twist & Bend$Bend", {arg parameterName; parameterName.containsi("bend");}],

			["Draw$Active & Max Size",{arg argName;
				var arrNames = ["drawActive", "maxWidthHeightRule", "maxDepthRule", "customMaxWidth",
					"customMaxHeight", "customMaxDepth",];
				arrNames.indexOfEqual(argName).notNil;}],
			["Draw$Scale",
				{arg parameterName;
					var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position & Anchor",
				{arg parameterName;
					var arrNames = ["positionX", "positionY", "positionZ",
						"drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY", "anchorZ",];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Rotate",
				{arg parameterName;
					var arrNames = ["rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
					arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Shape";
	}

	initExtraActions { // override
		// arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
	}

	extraSubModuleData { // override
		^[arrSlotData];
	}

	loadExtraSubModuleData {arg argData;  // override
		arrSlotData = argData[0];
	}

	getExtraSynthArgSpecs { //  override
		^[
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
	}

}


