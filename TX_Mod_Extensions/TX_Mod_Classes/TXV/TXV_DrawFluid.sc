// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawFluid : TXV_Module {
	classvar <defaultName = "TXV Fluid";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text to ... "TXDrawFluid_1/drawMode"
	// , ["Draw Colors", "Draw Motion", "Draw Speed", "Draw Vectors"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawFluid_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawFluid_1/drawActive", [
			["bool_int", "/TXDrawFluid_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/drawActive/useExtMod", 0],
			["float", "/TXDrawFluid_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawFluid_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawFluid_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawFluid_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXDrawFluid_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupBool", "/TXDrawFluid_1/drawFluid", [
			["bool_int", "/TXDrawFluid_1/drawFluid/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/drawFluid/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/drawFluid/useExtMod", 0],
			["float", "/TXDrawFluid_1/drawFluid/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawFluid_1/fluidCellsX", 150, 10, 1000],
		["int", "/TXDrawFluid_1/fluidCellsY", 150, 10, 1000],
		[ "modParameterGroupInt", "/TXDrawFluid_1/drawMode", [
			["int", "/TXDrawFluid_1/drawMode/fixedValue", 0, 0, 3, ["Draw Colors", "Draw Motion", "Draw Speed", "Draw Vectors"]],
			["float", "/TXDrawFluid_1/drawMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/drawMode/useExtMod", 0],
			["float", "/TXDrawFluid_1/drawMode/extModValue", 0, 0, 1],
			["int", "/TXDrawFluid_1/drawMode/softMin", 0, 0, 3],
			["int", "/TXDrawFluid_1/drawMode/softMax", 3, 0, 3],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/frozen", [
			["bool_int", "/TXDrawFluid_1/frozen/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/frozen/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/frozen/useExtMod", 0],
			["float", "/TXDrawFluid_1/frozen/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/viscocity", [
			["float", "/TXDrawFluid_1/viscocity/fixedValue", 0.01, 0, 0.1],
			["float", "/TXDrawFluid_1/viscocity/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/viscocity/useExtMod", 0],
			["float", "/TXDrawFluid_1/viscocity/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/viscocity/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/viscocity/softMax", 0.1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/colorDiffusion", [
			["float", "/TXDrawFluid_1/colorDiffusion/fixedValue", 0, 0, 0.3],
			["float", "/TXDrawFluid_1/colorDiffusion/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/colorDiffusion/useExtMod", 0],
			["float", "/TXDrawFluid_1/colorDiffusion/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/colorDiffusion/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/colorDiffusion/softMax", 0.3, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/colorMultiply", [
			["float", "/TXDrawFluid_1/colorMultiply/fixedValue", 1, 0, 100],
			["float", "/TXDrawFluid_1/colorMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/colorMultiply/useExtMod", 0],
			["float", "/TXDrawFluid_1/colorMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/colorMultiply/softMin", 0, 0, 1000],
			["float", "/TXDrawFluid_1/colorMultiply/softMax", 100, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/forceMultiply", [
			["float", "/TXDrawFluid_1/forceMultiply/fixedValue", 20, 0, 100],
			["float", "/TXDrawFluid_1/forceMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/forceMultiply/useExtMod", 0],
			["float", "/TXDrawFluid_1/forceMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/forceMultiply/softMin", 0, 0, 100],
			["float", "/TXDrawFluid_1/forceMultiply/softMax", 100, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/fadeSpeed", [
			["float", "/TXDrawFluid_1/fadeSpeed/fixedValue", 0.05, 0, 1],
			["float", "/TXDrawFluid_1/fadeSpeed/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/fadeSpeed/useExtMod", 0],
			["float", "/TXDrawFluid_1/fadeSpeed/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/fadeSpeed/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/fadeSpeed/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/deltaTime", [
			["float", "/TXDrawFluid_1/deltaTime/fixedValue", 1, 0.001, 5],
			["float", "/TXDrawFluid_1/deltaTime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/deltaTime/useExtMod", 0],
			["float", "/TXDrawFluid_1/deltaTime/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/deltaTime/softMin", 0.001, 0.001, 20],
			["float", "/TXDrawFluid_1/deltaTime/softMax", 5, 0.001, 20],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/wrapX", [
			["bool_int", "/TXDrawFluid_1/wrapX/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/wrapX/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/wrapX/useExtMod", 0],
			["float", "/TXDrawFluid_1/wrapX/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/wrapY", [
			["bool_int", "/TXDrawFluid_1/wrapY/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/wrapY/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/wrapY/useExtMod", 0],
			["float", "/TXDrawFluid_1/wrapY/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXDrawFluid_1/solverIterations", [
			["int", "/TXDrawFluid_1/solverIterations/fixedValue", 10, 1, 50],
			["float", "/TXDrawFluid_1/solverIterations/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/solverIterations/useExtMod", 0],
			["float", "/TXDrawFluid_1/solverIterations/extModValue", 0, 0, 1],
			["int", "/TXDrawFluid_1/solverIterations/softMin", 1, 1, 100],
			["int", "/TXDrawFluid_1/solverIterations/softMax", 50, 1, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/doVorticityConfinement", [
			["bool_int", "/TXDrawFluid_1/doVorticityConfinement/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/doVorticityConfinement/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/doVorticityConfinement/useExtMod", 0],
			["float", "/TXDrawFluid_1/doVorticityConfinement/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/doRGB", [
			["bool_int", "/TXDrawFluid_1/doRGB/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/doRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/doRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/doRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color1Active", [
			["bool_int", "/TXDrawFluid_1/color1Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color1Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color1Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1PosX", [
			["float", "/TXDrawFluid_1/color1PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1PosY", [
			["float", "/TXDrawFluid_1/color1PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1Hue", [
			["float", "/TXDrawFluid_1/color1Hue/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1Saturation", [
			["float", "/TXDrawFluid_1/color1Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/color1Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1Brightness", [
			["float", "/TXDrawFluid_1/color1Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/color1Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color1Alpha", [
			["float", "/TXDrawFluid_1/color1Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/color1Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color1Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color1Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color1HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/color1HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color1HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color1HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/color1HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color2Active", [
			["bool_int", "/TXDrawFluid_1/color2Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color2Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color2Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2PosX", [
			["float", "/TXDrawFluid_1/color2PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2PosY", [
			["float", "/TXDrawFluid_1/color2PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2Hue", [
			["float", "/TXDrawFluid_1/color2Hue/fixedValue", 0.25, 0, 1],
			["float", "/TXDrawFluid_1/color2Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2Saturation", [
			["float", "/TXDrawFluid_1/color2Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/color2Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2Brightness", [
			["float", "/TXDrawFluid_1/color2Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/color2Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color2Alpha", [
			["float", "/TXDrawFluid_1/color2Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/color2Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color2Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color2Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color2HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/color2HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color2HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color2HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/color2HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color3Active", [
			["bool_int", "/TXDrawFluid_1/color3Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color3Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color3Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3PosX", [
			["float", "/TXDrawFluid_1/color3PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3PosY", [
			["float", "/TXDrawFluid_1/color3PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3Hue", [
			["float", "/TXDrawFluid_1/color3Hue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/color3Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3Saturation", [
			["float", "/TXDrawFluid_1/color3Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/color3Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3Brightness", [
			["float", "/TXDrawFluid_1/color3Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/color3Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color3Alpha", [
			["float", "/TXDrawFluid_1/color3Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/color3Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color3Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color3Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color3HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/color3HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color3HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color3HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/color3HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color4Active", [
			["bool_int", "/TXDrawFluid_1/color4Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color4Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color4Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4PosX", [
			["float", "/TXDrawFluid_1/color4PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4PosY", [
			["float", "/TXDrawFluid_1/color4PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4Hue", [
			["float", "/TXDrawFluid_1/color4Hue/fixedValue", 0.75, 0, 1],
			["float", "/TXDrawFluid_1/color4Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4Saturation", [
			["float", "/TXDrawFluid_1/color4Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/color4Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4Brightness", [
			["float", "/TXDrawFluid_1/color4Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/color4Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/color4Alpha", [
			["float", "/TXDrawFluid_1/color4Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/color4Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/color4Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/color4Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/color4HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/color4HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/color4HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/color4HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/color4HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/force1Active", [
			["bool_int", "/TXDrawFluid_1/force1Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/force1Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/force1Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force1X", [
			["float", "/TXDrawFluid_1/force1X/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force1X/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1X/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1X/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force1X/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force1Y", [
			["float", "/TXDrawFluid_1/force1Y/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force1Y/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1Y/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force1Y/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce1YAsDirection", [
			["bool_int", "/TXDrawFluid_1/useForce1YAsDirection/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useForce1YAsDirection/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce1YAsDirection/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce1YAsDirection/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force1PosX", [
			["float", "/TXDrawFluid_1/force1PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force1PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force1PosY", [
			["float", "/TXDrawFluid_1/force1PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force1PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useColor1PosForForce1", [
			["bool_int", "/TXDrawFluid_1/useColor1PosForForce1/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useColor1PosForForce1/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useColor1PosForForce1/useExtMod", 0],
			["float", "/TXDrawFluid_1/useColor1PosForForce1/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force1PosSpeedToForce", [
			["float", "/TXDrawFluid_1/force1PosSpeedToForce/fixedValue", 0, -10, 10],
			["float", "/TXDrawFluid_1/force1PosSpeedToForce/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force1PosSpeedToForce/useExtMod", 0],
			["float", "/TXDrawFluid_1/force1PosSpeedToForce/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force1PosSpeedToForce/softMin", -10, -100, 100],
			["float", "/TXDrawFluid_1/force1PosSpeedToForce/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/force2Active", [
			["bool_int", "/TXDrawFluid_1/force2Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/force2Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/force2Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force2X", [
			["float", "/TXDrawFluid_1/force2X/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force2X/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2X/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2X/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force2X/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force2Y", [
			["float", "/TXDrawFluid_1/force2Y/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force2Y/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2Y/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force2Y/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce2YAsDirection", [
			["bool_int", "/TXDrawFluid_1/useForce2YAsDirection/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useForce2YAsDirection/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce2YAsDirection/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce2YAsDirection/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force2PosX", [
			["float", "/TXDrawFluid_1/force2PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force2PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force2PosY", [
			["float", "/TXDrawFluid_1/force2PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force2PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useColor2PosForForce2", [
			["bool_int", "/TXDrawFluid_1/useColor2PosForForce2/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useColor2PosForForce2/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useColor2PosForForce2/useExtMod", 0],
			["float", "/TXDrawFluid_1/useColor2PosForForce2/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force2PosSpeedToForce", [
			["float", "/TXDrawFluid_1/force2PosSpeedToForce/fixedValue", 0, -10, 10],
			["float", "/TXDrawFluid_1/force2PosSpeedToForce/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force2PosSpeedToForce/useExtMod", 0],
			["float", "/TXDrawFluid_1/force2PosSpeedToForce/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force2PosSpeedToForce/softMin", -10, -100, 100],
			["float", "/TXDrawFluid_1/force2PosSpeedToForce/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/force3Active", [
			["bool_int", "/TXDrawFluid_1/force3Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/force3Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/force3Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force3X", [
			["float", "/TXDrawFluid_1/force3X/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force3X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force3X/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3X/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3X/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force3X/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force3Y", [
			["float", "/TXDrawFluid_1/force3Y/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force3Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force3Y/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3Y/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3Y/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force3Y/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce3YAsDirection", [
			["bool_int", "/TXDrawFluid_1/useForce3YAsDirection/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useForce3YAsDirection/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce3YAsDirection/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce3YAsDirection/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force3PosX", [
			["float", "/TXDrawFluid_1/force3PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force3PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force3PosY", [
			["float", "/TXDrawFluid_1/force3PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force3PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useColor3PosForForce3", [
			["bool_int", "/TXDrawFluid_1/useColor3PosForForce3/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useColor3PosForForce3/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useColor3PosForForce3/useExtMod", 0],
			["float", "/TXDrawFluid_1/useColor3PosForForce3/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force3PosSpeedToForce", [
			["float", "/TXDrawFluid_1/force3PosSpeedToForce/fixedValue", 0, -10, 10],
			["float", "/TXDrawFluid_1/force3PosSpeedToForce/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force3PosSpeedToForce/useExtMod", 0],
			["float", "/TXDrawFluid_1/force3PosSpeedToForce/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force3PosSpeedToForce/softMin", -10, -100, 100],
			["float", "/TXDrawFluid_1/force3PosSpeedToForce/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/force4Active", [
			["bool_int", "/TXDrawFluid_1/force4Active/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/force4Active/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/force4Active/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4Active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force4X", [
			["float", "/TXDrawFluid_1/force4X/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force4X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force4X/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4X/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4X/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force4X/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force4Y", [
			["float", "/TXDrawFluid_1/force4Y/fixedValue", 0, -1, 1],
			["float", "/TXDrawFluid_1/force4Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force4Y/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4Y/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4Y/softMin", -1, -1, 1],
			["float", "/TXDrawFluid_1/force4Y/softMax", 1, -1, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce4YAsDirection", [
			["bool_int", "/TXDrawFluid_1/useForce4YAsDirection/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useForce4YAsDirection/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce4YAsDirection/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce4YAsDirection/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force4PosX", [
			["float", "/TXDrawFluid_1/force4PosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force4PosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4PosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force4PosY", [
			["float", "/TXDrawFluid_1/force4PosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force4PosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4PosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useColor4PosForForce4", [
			["bool_int", "/TXDrawFluid_1/useColor4PosForForce4/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/useColor4PosForForce4/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useColor4PosForForce4/useExtMod", 0],
			["float", "/TXDrawFluid_1/useColor4PosForForce4/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/force4PosSpeedToForce", [
			["float", "/TXDrawFluid_1/force4PosSpeedToForce/fixedValue", 0, -10, 10],
			["float", "/TXDrawFluid_1/force4PosSpeedToForce/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/force4PosSpeedToForce/useExtMod", 0],
			["float", "/TXDrawFluid_1/force4PosSpeedToForce/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/force4PosSpeedToForce/softMin", -10, -100, 100],
			["float", "/TXDrawFluid_1/force4PosSpeedToForce/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/width", [
			["float", "/TXDrawFluid_1/width/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/width/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/width/useExtMod", 0],
			["float", "/TXDrawFluid_1/width/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/width/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/width/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/height", [
			["float", "/TXDrawFluid_1/height/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/height/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/height/useExtMod", 0],
			["float", "/TXDrawFluid_1/height/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/height/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/height/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/positionX", [
			["float", "/TXDrawFluid_1/positionX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/positionX/useExtMod", 0],
			["float", "/TXDrawFluid_1/positionX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/positionY", [
			["float", "/TXDrawFluid_1/positionY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/positionY/useExtMod", 0],
			["float", "/TXDrawFluid_1/positionY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/positionY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/anchorX", [
			["float", "/TXDrawFluid_1/anchorX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/anchorX/useExtMod", 0],
			["float", "/TXDrawFluid_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/anchorY", [
			["float", "/TXDrawFluid_1/anchorY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/anchorY/useExtMod", 0],
			["float", "/TXDrawFluid_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/rotate", [
			["float", "/TXDrawFluid_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawFluid_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/rotate/useExtMod", 0],
			["float", "/TXDrawFluid_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawFluid_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/rotateMultiply", [
			["float", "/TXDrawFluid_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawFluid_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawFluid_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawFluid_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/scaleX", [
			["float", "/TXDrawFluid_1/scaleX/fixedValue", 1, 0, 10],
			["float", "/TXDrawFluid_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/scaleX/useExtMod", 0],
			["float", "/TXDrawFluid_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/scaleX/softMin", 0, -100, 100],
			["float", "/TXDrawFluid_1/scaleX/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/scaleY", [
			["float", "/TXDrawFluid_1/scaleY/fixedValue", 1, 0, 10],
			["float", "/TXDrawFluid_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/scaleY/useExtMod", 0],
			["float", "/TXDrawFluid_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/scaleY/softMin", 0, -100, 100],
			["float", "/TXDrawFluid_1/scaleY/softMax", 10, -100, 100],
		]],
		["bool_int", "/TXDrawFluid_1/useScaleXForScaleY", 0],
		[ "modParameterGroupBool", "/TXDrawFluid_1/drawParticles", [
			["bool_int", "/TXDrawFluid_1/drawParticles/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/drawParticles/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/drawParticles/useExtMod", 0],
			["float", "/TXDrawFluid_1/drawParticles/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/triggerParticlesContinuosly", [
			["bool_int", "/TXDrawFluid_1/triggerParticlesContinuosly/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/triggerParticlesContinuosly/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/triggerParticlesContinuosly/useExtMod", 0],
			["float", "/TXDrawFluid_1/triggerParticlesContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/triggerParticlesNow", [
			["bool_int", "/TXDrawFluid_1/triggerParticlesNow/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/triggerParticlesNow/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/triggerParticlesNow/useExtMod", 0],
			["float", "/TXDrawFluid_1/triggerParticlesNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce1ToTriggerParticles", [
			["bool_int", "/TXDrawFluid_1/useForce1ToTriggerParticles/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/useForce1ToTriggerParticles/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce1ToTriggerParticles/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce1ToTriggerParticles/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawFluid_1/maximumActiveParticles", 10000, 100, 50000],
		[ "modParameterGroupInt", "/TXDrawFluid_1/numParticlesPerTrigger", [
			["int", "/TXDrawFluid_1/numParticlesPerTrigger/fixedValue", 10, 0, 100],
			["float", "/TXDrawFluid_1/numParticlesPerTrigger/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/numParticlesPerTrigger/useExtMod", 0],
			["float", "/TXDrawFluid_1/numParticlesPerTrigger/extModValue", 0, 0, 1],
			["int", "/TXDrawFluid_1/numParticlesPerTrigger/softMin", 0, 0, 1000],
			["int", "/TXDrawFluid_1/numParticlesPerTrigger/softMax", 100, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleStartDelayRand", [
			["float", "/TXDrawFluid_1/particleStartDelayRand/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleStartDelayRand/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleStartDelayRand/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleStartDelayRand/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleStartDelayRand/softMin", 0, 0, 10],
			["float", "/TXDrawFluid_1/particleStartDelayRand/softMax", 1, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleLifetime", [
			["float", "/TXDrawFluid_1/particleLifetime/fixedValue", 10, 0, 10],
			["float", "/TXDrawFluid_1/particleLifetime/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleLifetime/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleLifetime/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleLifetime/softMin", 0, 0, 1000],
			["float", "/TXDrawFluid_1/particleLifetime/softMax", 10, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleLifetimeRandom", [
			["float", "/TXDrawFluid_1/particleLifetimeRandom/fixedValue", 2, 0, 10],
			["float", "/TXDrawFluid_1/particleLifetimeRandom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleLifetimeRandom/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleLifetimeRandom/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleLifetimeRandom/softMin", 0, 0, 1000],
			["float", "/TXDrawFluid_1/particleLifetimeRandom/softMax", 10, 0, 1000],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/killAllParticlesNow", [
			["bool_int", "/TXDrawFluid_1/killAllParticlesNow/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/killAllParticlesNow/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/killAllParticlesNow/useExtMod", 0],
			["float", "/TXDrawFluid_1/killAllParticlesNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleSpreadDiameter", [
			["float", "/TXDrawFluid_1/particleSpreadDiameter/fixedValue", 0.01, 0, 1],
			["float", "/TXDrawFluid_1/particleSpreadDiameter/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleSpreadDiameter/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleSpreadDiameter/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleSpreadDiameter/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleSpreadDiameter/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particlePosX", [
			["float", "/TXDrawFluid_1/particlePosX/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particlePosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/particlePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particlePosY", [
			["float", "/TXDrawFluid_1/particlePosY/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particlePosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/particlePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particlePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/useForce1PosForParticles", [
			["bool_int", "/TXDrawFluid_1/useForce1PosForParticles/fixedValue", 1],
			["bool_int", "/TXDrawFluid_1/useForce1PosForParticles/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/useForce1PosForParticles/useExtMod", 0],
			["float", "/TXDrawFluid_1/useForce1PosForParticles/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/randomParticlePosX", [
			["float", "/TXDrawFluid_1/randomParticlePosX/fixedValue", 0.01, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/randomParticlePosX/useExtMod", 0],
			["float", "/TXDrawFluid_1/randomParticlePosX/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosX/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/randomParticlePosY", [
			["float", "/TXDrawFluid_1/randomParticlePosY/fixedValue", 0.01, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/randomParticlePosY/useExtMod", 0],
			["float", "/TXDrawFluid_1/randomParticlePosY/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosY/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/randomParticlePosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/lockParticleColorsAtStart", [
			["bool_int", "/TXDrawFluid_1/lockParticleColorsAtStart/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/lockParticleColorsAtStart/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/lockParticleColorsAtStart/useExtMod", 0],
			["float", "/TXDrawFluid_1/lockParticleColorsAtStart/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleSpeedXAlpha", [
			["float", "/TXDrawFluid_1/particleSpeedXAlpha/fixedValue", 0, 0, 10],
			["float", "/TXDrawFluid_1/particleSpeedXAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleSpeedXAlpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleSpeedXAlpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleSpeedXAlpha/softMin", 0, -100, 100],
			["float", "/TXDrawFluid_1/particleSpeedXAlpha/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleSpeedXBrightness", [
			["float", "/TXDrawFluid_1/particleSpeedXBrightness/fixedValue", 0, 0, 10],
			["float", "/TXDrawFluid_1/particleSpeedXBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleSpeedXBrightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleSpeedXBrightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleSpeedXBrightness/softMin", 0, -100, 100],
			["float", "/TXDrawFluid_1/particleSpeedXBrightness/softMax", 10, -100, 100],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor1Hue", [
			["float", "/TXDrawFluid_1/particleColor1Hue/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor1Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor1Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor1Saturation", [
			["float", "/TXDrawFluid_1/particleColor1Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor1Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor1Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor1Brightness", [
			["float", "/TXDrawFluid_1/particleColor1Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor1Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor1Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor1Alpha", [
			["float", "/TXDrawFluid_1/particleColor1Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor1Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor1Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor1Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/particleColor1HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/particleColor1HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/particleColor1HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/particleColor1HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor1HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor2Hue", [
			["float", "/TXDrawFluid_1/particleColor2Hue/fixedValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor2Hue/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor2Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Hue/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor2Saturation", [
			["float", "/TXDrawFluid_1/particleColor2Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor2Saturation/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor2Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor2Brightness", [
			["float", "/TXDrawFluid_1/particleColor2Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor2Brightness/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor2Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawFluid_1/particleColor2Alpha", [
			["float", "/TXDrawFluid_1/particleColor2Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawFluid_1/particleColor2Alpha/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor2Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawFluid_1/particleColor2Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawFluid_1/particleColor2HSBAsRGB", [
			["bool_int", "/TXDrawFluid_1/particleColor2HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawFluid_1/particleColor2HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawFluid_1/particleColor2HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawFluid_1/particleColor2HSBAsRGB/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDrawFluid_1/particleColorMixCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawFluid_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			// ["Forces", {arg parameterName;
			// 	parameterName.containsi("force")
			// 	&& (parameterName.containsi("forceMultiply") == false)
			// 	&& (parameterName.containsi("Particle") == false)
			// ;}],
			// ["Fluid Colors", {arg parameterName;
			// 	parameterName.containsi("color")
			// 	&& (parameterName.containsi("colorMultiply") == false)
			// 	&& (parameterName.containsi("colorDiffusion") == false)
			// 	&& (parameterName.containsi("Particle") == false)
			// ;}],
			// ["Particles", {arg parameterName;
			// 	parameterName.containsi("Particle")
			// 	&& (parameterName.containsi("ParticleColor") == false)
			// ;}],
			// ["Particle Colors", {arg parameterName;
			// 	parameterName.containsi("ParticleColor")
			// ;}],
			// ["Draw Parameters", {arg parameterName;
			// 	var drawParameterNames = ["drawActive", "maxWidthHeightRule", "customMaxWidth", "customMaxHeight", "customMaxDepth", "useSamplePosForDrawPos", "useSampleSizeForDrawSize", "constrainToEdges", "alphaThreshold", "alpha", "drawAlpha", "drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ", "drawWidth", "drawHeight", "anchorX", "anchorY", "rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", "scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ","width", "height" ];
			// 	// return validity bool
			// 	drawParameterNames.indexOfEqual(parameterName).notNil;
			// }],
			["Fluid$Global", {arg parameterName;
				var arrNames = ["drawFluid", "fluidCellsX", "fluidCellsY", "drawMode", "frozen", "viscocity", "colorDiffusion", "colorMultiply", "forceMultiply", "fadeSpeed", "deltaTime", "wrapX", "wrapY", "solverIterations", "doVorticityConfinement", "doRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Force 1", {arg parameterName;
				var arrNames = ["force1Active", "force1X", "force1Y", "useForce1YAsDirection", "force1PosX", "force1PosY", "useColor1PosForForce1", "force1PosSpeedToForce", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Force 2", {arg parameterName;
				var arrNames = ["force2Active", "force2X", "force2Y", "useForce2YAsDirection", "force2PosX", "force2PosY", "useColor2PosForForce2", "force2PosSpeedToForce", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Force 3", {arg parameterName;
				var arrNames = ["force3Active", "force3X", "force3Y", "useForce3YAsDirection", "force3PosX", "force3PosY", "useColor3PosForForce3", "force3PosSpeedToForce", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Force 4", {arg parameterName;
				var arrNames = ["force4Active", "force4X", "force4Y", "useForce4YAsDirection", "force4PosX", "force4PosY", "useColor4PosForForce4", "force4PosSpeedToForce", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Color 1", {arg parameterName;
				var arrNames = ["color1Active", "color1PosX", "color1PosY", "color1Hue", "color1Saturation", "color1Brightness", "color1Alpha", "color1HSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Color 2", {arg parameterName;
				var arrNames = ["color2Active", "color2PosX", "color2PosY", "color2Hue", "color2Saturation", "color2Brightness", "color2Alpha", "color2HSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Color 3", {arg parameterName;
				var arrNames = ["color3Active", "color3PosX", "color3PosY", "color3Hue", "color3Saturation", "color3Brightness", "color3Alpha", "color3HSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Fluid$Color 4", {arg parameterName;
				var arrNames = ["color4Active", "color4PosX", "color4PosY", "color4Hue", "color4Saturation", "color4Brightness", "color4Alpha", "color4HSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Global", {arg parameterName;
				var arrNames = ["drawParticles", "triggerParticlesContinuosly", "triggerParticlesNow", "useForce1ToTriggerParticles", "maximumActiveParticles", "numParticlesPerTrigger", "particleStartDelayRand", "killAllParticlesNow", "lockParticleColorsAtStart", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Lifetime & Speed Variation", {arg parameterName;
				var arrNames = ["particleLifetime", "particleLifetimeRandom", "particleSpeedXAlpha", "particleSpeedXBrightness", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Spread & Position", {arg parameterName;
				var arrNames = ["particleSpreadDiameter", "particlePosX", "particlePosY", "useForce1PosForParticles", "randomParticlePosX", "randomParticlePosY", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Color 1", {arg parameterName;
				var arrNames = ["particleColor1Hue", "particleColor1Saturation", "particleColor1Brightness", "particleColor1Alpha", "particleColor1HSBAsRGB", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Color 2", {arg parameterName;
				var arrNames = ["particleColor2Hue", "particleColor2Saturation", "particleColor2Brightness", "particleColor2Alpha", "particleColor2HSBAsRGB",];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Particles$Color Mix Curve", {arg parameterName;
				parameterName.containsi("particleColorMixCurve")
			}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "width", "height",  ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["positionX", "positionY", "positionZ", "anchorX", "anchorY",
					"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Fluid";
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


