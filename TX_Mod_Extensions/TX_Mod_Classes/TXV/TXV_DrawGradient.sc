// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawGradient : TXV_Module {
	classvar <defaultName = "TXV Gradient";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]

	// add text array to ..."gradientType/fixedValue":
	// , ["horizontal", "vertical", "radial",]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawGradient_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawGradient_1/drawActive", [
			["bool_int", "/TXDrawGradient_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawGradient_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/drawActive/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawGradient_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawGradient_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawGradient_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXDrawGradient_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupBool", "/TXDrawGradient_1/renderContinuosly", [
			["bool_int", "/TXDrawGradient_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXDrawGradient_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/renderContinuosly/useExtMod", 0],
			["float", "/TXDrawGradient_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/renderNow", [
			["bool_int", "/TXDrawGradient_1/renderNow/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/renderNow/useExtMod", 0],
			["float", "/TXDrawGradient_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawGradient_1/renderWidthHeightRule", 0, 0, 11, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "360p (SD) 480 x 360", "480p (SD) 858 x 480", "720p (Half HD)1280 x 720", "1080p (Full HD) 1920 x 1080", "1440p (2k) 2560 x 1440", "2160p (4k) 3840 x 2160", ]],
		["int", "/TXDrawGradient_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXDrawGradient_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawGradient_1/gradientType", [
			["int", "/TXDrawGradient_1/gradientType/fixedValue", 0, 0, 2, ["horizontal", "vertical", "radial",]],
			["float", "/TXDrawGradient_1/gradientType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/gradientType/useExtMod", 0],
			["float", "/TXDrawGradient_1/gradientType/extModValue", 0, 0, 1],
			["int", "/TXDrawGradient_1/gradientType/softMin", 0, 0, 2],
			["int", "/TXDrawGradient_1/gradientType/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupInt", "/TXDrawGradient_1/numColors", [
			["int", "/TXDrawGradient_1/numColors/fixedValue", 4, 2, 4],
			["float", "/TXDrawGradient_1/numColors/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/numColors/useExtMod", 0],
			["float", "/TXDrawGradient_1/numColors/extModValue", 0, 0, 1],
			["int", "/TXDrawGradient_1/numColors/softMin", 2, 2, 4],
			["int", "/TXDrawGradient_1/numColors/softMax", 4, 2, 4],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/curveBias", [
			["float", "/TXDrawGradient_1/curveBias/fixedValue", 0, -2, 2],
			["float", "/TXDrawGradient_1/curveBias/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/curveBias/useExtMod", 0],
			["float", "/TXDrawGradient_1/curveBias/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/curveBias/softMin", -2, -10, 10],
			["float", "/TXDrawGradient_1/curveBias/softMax", 2, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/invert", [
			["bool_int", "/TXDrawGradient_1/invert/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/invert/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/invert/useExtMod", 0],
			["float", "/TXDrawGradient_1/invert/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/point1X", [
			["float", "/TXDrawGradient_1/point1X/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point1X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/point1X/useExtMod", 0],
			["float", "/TXDrawGradient_1/point1X/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point1X/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/point1X/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/point1Y", [
			["float", "/TXDrawGradient_1/point1Y/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point1Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/point1Y/useExtMod", 0],
			["float", "/TXDrawGradient_1/point1Y/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point1Y/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/point1Y/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/point2X", [
			["float", "/TXDrawGradient_1/point2X/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/point2X/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/point2X/useExtMod", 0],
			["float", "/TXDrawGradient_1/point2X/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point2X/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/point2X/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/point2Y", [
			["float", "/TXDrawGradient_1/point2Y/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/point2Y/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/point2Y/useExtMod", 0],
			["float", "/TXDrawGradient_1/point2Y/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/point2Y/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/point2Y/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color1Hue", [
			["float", "/TXDrawGradient_1/color1Hue/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color1Hue/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Hue/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color1HueRotate", [
			["float", "/TXDrawGradient_1/color1HueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color1HueRotate/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1HueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1HueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color1Saturation", [
			["float", "/TXDrawGradient_1/color1Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawGradient_1/color1Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color1Saturation/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color1Brightness", [
			["float", "/TXDrawGradient_1/color1Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/color1Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color1Brightness/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color1Alpha", [
			["float", "/TXDrawGradient_1/color1Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/color1Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color1Alpha/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color1Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/color1HSBAsRGB", [
			["bool_int", "/TXDrawGradient_1/color1HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/color1HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/color1HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawGradient_1/color1HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color2Hue", [
			["float", "/TXDrawGradient_1/color2Hue/fixedValue", 0.25, 0, 1],
			["float", "/TXDrawGradient_1/color2Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color2Hue/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Hue/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color2HueRotate", [
			["float", "/TXDrawGradient_1/color2HueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color2HueRotate/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2HueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2HueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color2Saturation", [
			["float", "/TXDrawGradient_1/color2Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawGradient_1/color2Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color2Saturation/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color2Brightness", [
			["float", "/TXDrawGradient_1/color2Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/color2Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color2Brightness/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color2Alpha", [
			["float", "/TXDrawGradient_1/color2Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/color2Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color2Alpha/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color2Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/color2HSBAsRGB", [
			["bool_int", "/TXDrawGradient_1/color2HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/color2HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/color2HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawGradient_1/color2HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color3Hue", [
			["float", "/TXDrawGradient_1/color3Hue/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/color3Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color3Hue/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Hue/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color3HueRotate", [
			["float", "/TXDrawGradient_1/color3HueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color3HueRotate/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3HueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3HueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color3Saturation", [
			["float", "/TXDrawGradient_1/color3Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawGradient_1/color3Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color3Saturation/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color3Brightness", [
			["float", "/TXDrawGradient_1/color3Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/color3Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color3Brightness/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color3Alpha", [
			["float", "/TXDrawGradient_1/color3Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/color3Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color3Alpha/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color3Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/color3HSBAsRGB", [
			["bool_int", "/TXDrawGradient_1/color3HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/color3HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/color3HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawGradient_1/color3HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color4Hue", [
			["float", "/TXDrawGradient_1/color4Hue/fixedValue", 0.75, 0, 1],
			["float", "/TXDrawGradient_1/color4Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color4Hue/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4Hue/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Hue/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color4HueRotate", [
			["float", "/TXDrawGradient_1/color4HueRotate/fixedValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color4HueRotate/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4HueRotate/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4HueRotate/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color4Saturation", [
			["float", "/TXDrawGradient_1/color4Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXDrawGradient_1/color4Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color4Saturation/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4Saturation/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Saturation/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color4Brightness", [
			["float", "/TXDrawGradient_1/color4Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/color4Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color4Brightness/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4Brightness/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Brightness/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/color4Alpha", [
			["float", "/TXDrawGradient_1/color4Alpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/color4Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/color4Alpha/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4Alpha/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Alpha/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/color4Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXDrawGradient_1/color4HSBAsRGB", [
			["bool_int", "/TXDrawGradient_1/color4HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXDrawGradient_1/color4HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXDrawGradient_1/color4HSBAsRGB/useExtMod", 0],
			["float", "/TXDrawGradient_1/color4HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/drawAlpha", [
			["float", "/TXDrawGradient_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/drawAlpha/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/drawPosX", [
			["float", "/TXDrawGradient_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/drawPosX/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/drawPosY", [
			["float", "/TXDrawGradient_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/drawPosY/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/drawWidth", [
			["float", "/TXDrawGradient_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/drawWidth/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/drawHeight", [
			["float", "/TXDrawGradient_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXDrawGradient_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/drawHeight/useExtMod", 0],
			["float", "/TXDrawGradient_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/anchorX", [
			["float", "/TXDrawGradient_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/anchorX/useExtMod", 0],
			["float", "/TXDrawGradient_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/anchorY", [
			["float", "/TXDrawGradient_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawGradient_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/anchorY/useExtMod", 0],
			["float", "/TXDrawGradient_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXDrawGradient_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/rotate", [
			["float", "/TXDrawGradient_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXDrawGradient_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/rotate/useExtMod", 0],
			["float", "/TXDrawGradient_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/rotate/softMin", -360, -360, 360],
			["float", "/TXDrawGradient_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/rotateMultiply", [
			["float", "/TXDrawGradient_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawGradient_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawGradient_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawGradient_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/scaleX", [
			["float", "/TXDrawGradient_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawGradient_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/scaleX/useExtMod", 0],
			["float", "/TXDrawGradient_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawGradient_1/scaleY", [
			["float", "/TXDrawGradient_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawGradient_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawGradient_1/scaleY/useExtMod", 0],
			["float", "/TXDrawGradient_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawGradient_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawGradient_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXDrawGradient_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawGradient_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Points", {arg parameterName;
				parameterName.containsi("point");
			}],
			["Colors$Color 1", {arg parameterName;
				parameterName.containsi("color1");
			}],
			["Colors$Color 2", {arg parameterName;
				parameterName.containsi("color2");
			}],
			["Colors$Color 3", {arg parameterName;
				parameterName.containsi("color3");
			}],
			["Colors$Color 4", {arg parameterName;
				parameterName.containsi("color4");
			}],
			["Render", {arg parameterName;
				parameterName.containsi("render");
			}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth", "useSamplePosForDrawPos",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ",
				"anchorX", "anchorY", "rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Gradient";
	}

}

