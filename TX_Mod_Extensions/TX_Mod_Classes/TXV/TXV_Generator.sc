// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Generator : TXV_Module {
	classvar <defaultName = "TXV Generator";
	classvar <>arrInstances;

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."generatorType/fixedValue":
	// , ["70s Pattern - uses zoom", "Caramel Clouds", "Clouds", "Cosmos", "Dark Cloud", "Dense Bubbles - uses color 1", "Dense Cloud - uses point, zoom, color 1", "Fire-Ball - uses point, zoom, color 1", "Flow - uses zoom", "Fog - uses zoom, colors 1-4", "Fog 2 - uses zoom, colors 1-4", "Hypnotic Color", "Mountains Texture - uses zoom, color 1", "Noise - uses zoom, colors 1-4", "Particles", "Pattern 1 - uses zoom, color 1", "Pattern 2 - uses zoom, color 1", "Snow", "Space Warp", "Voronoi - uses zoom, colors 1-3", "Voronoi Static - uses point, zoom, color 1", "Waves Cascade" ]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXGenerator_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupInt", "/TXGenerator_1/generatorType", [
			["int", "/TXGenerator_1/generatorType/fixedValue", 2, 0, 21, ["70s Pattern - uses zoom", "Caramel Clouds", "Clouds", "Cosmos", "Dark Cloud", "Dense Bubbles - uses color 1", "Dense Cloud - uses point, zoom, color 1", "Fire-Ball - uses point, zoom, color 1", "Flow - uses zoom", "Fog - uses zoom, colors 1-4", "Fog 2 - uses zoom, colors 1-4", "Hypnotic Color", "Mountains Texture - uses zoom, color 1", "Noise - uses zoom, colors 1-4", "Particles", "Pattern 1 - uses zoom, color 1", "Pattern 2 - uses zoom, color 1", "Snow", "Space Warp", "Voronoi - uses zoom, colors 1-3", "Voronoi Static - uses point, zoom, color 1", "Waves Cascade" ]],
			["float", "/TXGenerator_1/generatorType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/generatorType/useExtMod", 0],
			["float", "/TXGenerator_1/generatorType/extModValue", 0, 0, 1],
			["int", "/TXGenerator_1/generatorType/softMin", 0, 0, 21],
			["int", "/TXGenerator_1/generatorType/softMax", 21, 0, 21],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/drawActive", [
			["bool_int", "/TXGenerator_1/drawActive/fixedValue", 1],
			["bool_int", "/TXGenerator_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/drawActive/useExtMod", 0],
			["float", "/TXGenerator_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXGenerator_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXGenerator_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXGenerator_1/customMaxHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXGenerator_1/renderContinuosly", [
			["bool_int", "/TXGenerator_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXGenerator_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/renderContinuosly/useExtMod", 0],
			["float", "/TXGenerator_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/renderNow", [
			["bool_int", "/TXGenerator_1/renderNow/fixedValue", 0],
			["bool_int", "/TXGenerator_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/renderNow/useExtMod", 0],
			["float", "/TXGenerator_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/clearBeforeRender", [
			["bool_int", "/TXGenerator_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXGenerator_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXGenerator_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		["int", "/TXGenerator_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXGenerator_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXGenerator_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXGenerator_1/clearNow", [
			["bool_int", "/TXGenerator_1/clearNow/fixedValue", 0],
			["bool_int", "/TXGenerator_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/clearNow/useExtMod", 0],
			["float", "/TXGenerator_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/clearColorHue", [
			["float", "/TXGenerator_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/clearColorHue/useExtMod", 0],
			["float", "/TXGenerator_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/clearColorSaturation", [
			["float", "/TXGenerator_1/clearColorSaturation/fixedValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXGenerator_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/clearColorBrightness", [
			["float", "/TXGenerator_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXGenerator_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/clearColorAlpha", [
			["float", "/TXGenerator_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXGenerator_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/clearColorHSBAsRGB", [
			["bool_int", "/TXGenerator_1/clearColorHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXGenerator_1/clearColorHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/clearColorHSBAsRGB/useExtMod", 0],
			["float", "/TXGenerator_1/clearColorHSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/bpm", [
			["float", "/TXGenerator_1/bpm/fixedValue", 60, 0.1, 200],
			["float", "/TXGenerator_1/bpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/bpm/useExtMod", 0],
			["float", "/TXGenerator_1/bpm/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/bpm/softMin", 0.1, 0.001, 1000],
			["float", "/TXGenerator_1/bpm/softMax", 200, 0.001, 1000],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/useMasterBpm", [
			["bool_int", "/TXGenerator_1/useMasterBpm/fixedValue", 0],
			["bool_int", "/TXGenerator_1/useMasterBpm/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/useMasterBpm/useExtMod", 0],
			["float", "/TXGenerator_1/useMasterBpm/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/cycleTimeBeats", [
			["float", "/TXGenerator_1/cycleTimeBeats/fixedValue", 1, 1, 64],
			["float", "/TXGenerator_1/cycleTimeBeats/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/cycleTimeBeats/useExtMod", 0],
			["float", "/TXGenerator_1/cycleTimeBeats/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/cycleTimeBeats/softMin", 1, 0.01, 1000],
			["float", "/TXGenerator_1/cycleTimeBeats/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/cycleTimeDivisor", [
			["float", "/TXGenerator_1/cycleTimeDivisor/fixedValue", 1, 1, 64],
			["float", "/TXGenerator_1/cycleTimeDivisor/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/cycleTimeDivisor/useExtMod", 0],
			["float", "/TXGenerator_1/cycleTimeDivisor/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/cycleTimeDivisor/softMin", 1, 0.01, 1000],
			["float", "/TXGenerator_1/cycleTimeDivisor/softMax", 64, 0.01, 1000],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/frozen", [
			["bool_int", "/TXGenerator_1/frozen/fixedValue", 0],
			["bool_int", "/TXGenerator_1/frozen/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/frozen/useExtMod", 0],
			["float", "/TXGenerator_1/frozen/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/phaseOffset", [
			["float", "/TXGenerator_1/phaseOffset/fixedValue", 0, 0, 100],
			["float", "/TXGenerator_1/phaseOffset/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/phaseOffset/useExtMod", 0],
			["float", "/TXGenerator_1/phaseOffset/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/phaseOffset/softMin", 0, 0, 1000],
			["float", "/TXGenerator_1/phaseOffset/softMax", 100, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/pointX", [
			["float", "/TXGenerator_1/pointX/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/pointX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/pointX/useExtMod", 0],
			["float", "/TXGenerator_1/pointX/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/pointX/softMin", 0, 0, 1000],
			["float", "/TXGenerator_1/pointX/softMax", 1, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/pointY", [
			["float", "/TXGenerator_1/pointY/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/pointY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/pointY/useExtMod", 0],
			["float", "/TXGenerator_1/pointY/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/pointY/softMin", 0, 0, 1000],
			["float", "/TXGenerator_1/pointY/softMax", 1, 0, 1000],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/zoom", [
			["float", "/TXGenerator_1/zoom/fixedValue", 1, 0, 10],
			["float", "/TXGenerator_1/zoom/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/zoom/useExtMod", 0],
			["float", "/TXGenerator_1/zoom/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/zoom/softMin", 0, 0, 100],
			["float", "/TXGenerator_1/zoom/softMax", 10, 0, 100],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color1Hue", [
			["float", "/TXGenerator_1/color1Hue/fixedValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color1Hue/useExtMod", 0],
			["float", "/TXGenerator_1/color1Hue/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1Hue/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color1Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color1HueRotate", [
			["float", "/TXGenerator_1/color1HueRotate/fixedValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color1HueRotate/useExtMod", 0],
			["float", "/TXGenerator_1/color1HueRotate/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1HueRotate/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color1HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color1Saturation", [
			["float", "/TXGenerator_1/color1Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXGenerator_1/color1Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color1Saturation/useExtMod", 0],
			["float", "/TXGenerator_1/color1Saturation/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1Saturation/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color1Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color1Brightness", [
			["float", "/TXGenerator_1/color1Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color1Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color1Brightness/useExtMod", 0],
			["float", "/TXGenerator_1/color1Brightness/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1Brightness/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color1Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color1Alpha", [
			["float", "/TXGenerator_1/color1Alpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/color1Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color1Alpha/useExtMod", 0],
			["float", "/TXGenerator_1/color1Alpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color1Alpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color1Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/color1HSBAsRGB", [
			["bool_int", "/TXGenerator_1/color1HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXGenerator_1/color1HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/color1HSBAsRGB/useExtMod", 0],
			["float", "/TXGenerator_1/color1HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color2Hue", [
			["float", "/TXGenerator_1/color2Hue/fixedValue", 0.25, 0, 1],
			["float", "/TXGenerator_1/color2Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color2Hue/useExtMod", 0],
			["float", "/TXGenerator_1/color2Hue/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color2Hue/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color2Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color2HueRotate", [
			["float", "/TXGenerator_1/color2HueRotate/fixedValue", 0.25, 0, 1],
			["float", "/TXGenerator_1/color2HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color2HueRotate/useExtMod", 0],
			["float", "/TXGenerator_1/color2HueRotate/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color2HueRotate/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color2HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color2Saturation", [
			["float", "/TXGenerator_1/color2Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXGenerator_1/color2Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color2Saturation/useExtMod", 0],
			["float", "/TXGenerator_1/color2Saturation/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color2Saturation/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color2Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color2Brightness", [
			["float", "/TXGenerator_1/color2Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color2Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color2Brightness/useExtMod", 0],
			["float", "/TXGenerator_1/color2Brightness/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color2Brightness/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color2Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color2Alpha", [
			["float", "/TXGenerator_1/color2Alpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/color2Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color2Alpha/useExtMod", 0],
			["float", "/TXGenerator_1/color2Alpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color2Alpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color2Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/color2HSBAsRGB", [
			["bool_int", "/TXGenerator_1/color2HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXGenerator_1/color2HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/color2HSBAsRGB/useExtMod", 0],
			["float", "/TXGenerator_1/color2HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color3Hue", [
			["float", "/TXGenerator_1/color3Hue/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color3Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color3Hue/useExtMod", 0],
			["float", "/TXGenerator_1/color3Hue/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color3Hue/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color3Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color3HueRotate", [
			["float", "/TXGenerator_1/color3HueRotate/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color3HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color3HueRotate/useExtMod", 0],
			["float", "/TXGenerator_1/color3HueRotate/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color3HueRotate/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color3HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color3Saturation", [
			["float", "/TXGenerator_1/color3Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXGenerator_1/color3Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color3Saturation/useExtMod", 0],
			["float", "/TXGenerator_1/color3Saturation/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color3Saturation/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color3Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color3Brightness", [
			["float", "/TXGenerator_1/color3Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color3Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color3Brightness/useExtMod", 0],
			["float", "/TXGenerator_1/color3Brightness/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color3Brightness/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color3Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color3Alpha", [
			["float", "/TXGenerator_1/color3Alpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/color3Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color3Alpha/useExtMod", 0],
			["float", "/TXGenerator_1/color3Alpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color3Alpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color3Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/color3HSBAsRGB", [
			["bool_int", "/TXGenerator_1/color3HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXGenerator_1/color3HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/color3HSBAsRGB/useExtMod", 0],
			["float", "/TXGenerator_1/color3HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color4Hue", [
			["float", "/TXGenerator_1/color4Hue/fixedValue", 0.75, 0, 1],
			["float", "/TXGenerator_1/color4Hue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color4Hue/useExtMod", 0],
			["float", "/TXGenerator_1/color4Hue/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color4Hue/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color4Hue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color4HueRotate", [
			["float", "/TXGenerator_1/color4HueRotate/fixedValue", 0.75, 0, 1],
			["float", "/TXGenerator_1/color4HueRotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color4HueRotate/useExtMod", 0],
			["float", "/TXGenerator_1/color4HueRotate/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color4HueRotate/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color4HueRotate/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color4Saturation", [
			["float", "/TXGenerator_1/color4Saturation/fixedValue", 0.6, 0, 1],
			["float", "/TXGenerator_1/color4Saturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color4Saturation/useExtMod", 0],
			["float", "/TXGenerator_1/color4Saturation/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color4Saturation/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color4Saturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color4Brightness", [
			["float", "/TXGenerator_1/color4Brightness/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/color4Brightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color4Brightness/useExtMod", 0],
			["float", "/TXGenerator_1/color4Brightness/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color4Brightness/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color4Brightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/color4Alpha", [
			["float", "/TXGenerator_1/color4Alpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/color4Alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/color4Alpha/useExtMod", 0],
			["float", "/TXGenerator_1/color4Alpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/color4Alpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/color4Alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/drawAlpha", [
			["float", "/TXGenerator_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/drawAlpha/useExtMod", 0],
			["float", "/TXGenerator_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXGenerator_1/color4HSBAsRGB", [
			["bool_int", "/TXGenerator_1/color4HSBAsRGB/fixedValue", 0],
			["bool_int", "/TXGenerator_1/color4HSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXGenerator_1/color4HSBAsRGB/useExtMod", 0],
			["float", "/TXGenerator_1/color4HSBAsRGB/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/drawPosX", [
			["float", "/TXGenerator_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/drawPosX/useExtMod", 0],
			["float", "/TXGenerator_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/drawPosY", [
			["float", "/TXGenerator_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/drawPosY/useExtMod", 0],
			["float", "/TXGenerator_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/drawWidth", [
			["float", "/TXGenerator_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/drawWidth/useExtMod", 0],
			["float", "/TXGenerator_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/drawHeight", [
			["float", "/TXGenerator_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXGenerator_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/drawHeight/useExtMod", 0],
			["float", "/TXGenerator_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/anchorX", [
			["float", "/TXGenerator_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/anchorX/useExtMod", 0],
			["float", "/TXGenerator_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/anchorY", [
			["float", "/TXGenerator_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXGenerator_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/anchorY/useExtMod", 0],
			["float", "/TXGenerator_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXGenerator_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/rotate", [
			["float", "/TXGenerator_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXGenerator_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/rotate/useExtMod", 0],
			["float", "/TXGenerator_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/rotate/softMin", -360, -360, 360],
			["float", "/TXGenerator_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/rotateMultiply", [
			["float", "/TXGenerator_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXGenerator_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/rotateMultiply/useExtMod", 0],
			["float", "/TXGenerator_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXGenerator_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/scaleX", [
			["float", "/TXGenerator_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXGenerator_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/scaleX/useExtMod", 0],
			["float", "/TXGenerator_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXGenerator_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXGenerator_1/scaleY", [
			["float", "/TXGenerator_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXGenerator_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXGenerator_1/scaleY/useExtMod", 0],
			["float", "/TXGenerator_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXGenerator_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXGenerator_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXGenerator_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXGenerator_1 ---------- */
		// empty
	];}


	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["BPM, Cycle, Phase", {arg parameterName;
				parameterName.containsi("bpm")
				|| parameterName.containsi("cycleTime")
				|| parameterName.containsi("phaseOffset")
				|| parameterName.containsi("frozen")
				;}],
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
			["Clear & Render$Clear", {arg parameterName;
				parameterName.containsi("clear")
				;}],
			["Clear & Render$Render", {arg parameterName;
				parameterName.containsi("render")
				&& not(parameterName.containsi("clear"))
				|| parameterName.containsi("drawLayersRule")
				;}],
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
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
					"useSamplePosForDrawPos","anchorX", "anchorY", "anchorZ", "rotate", "rotateX", "rotateY", "rotateZ",
					"rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Generator";
	}

}

