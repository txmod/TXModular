// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_MeshDistortHemi : TXV_Module {
	classvar <defaultName = "TXV MeshDistort Hemi";
	classvar <>arrInstances;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXMeshDistort_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXMeshDistort_1/drawActive", [
			["bool_int", "/TXMeshDistort_1/drawActive/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/drawActive/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXMeshDistort_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXMeshDistort_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXMeshDistort_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXMeshDistort_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXMeshDistort_1/drawLayersRule", [
			["int", "/TXMeshDistort_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXMeshDistort_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawLayersRule/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXMeshDistort_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXMeshDistort_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/renderContinuosly", [
			["bool_int", "/TXMeshDistort_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/renderContinuosly/useExtMod", 0],
			["float", "/TXMeshDistort_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/renderNow", [
			["bool_int", "/TXMeshDistort_1/renderNow/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/renderNow/useExtMod", 0],
			["float", "/TXMeshDistort_1/renderNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/clearBeforeRender", [
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/clearNow", [
			["bool_int", "/TXMeshDistort_1/clearNow/fixedValue", 0],
			["bool_int", "/TXMeshDistort_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/clearNow/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorHue", [
			["float", "/TXMeshDistort_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorHue/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXMeshDistort_1/useHemiLight", [
			["bool_int", "/TXMeshDistort_1/useHemiLight/fixedValue", 1],
			["bool_int", "/TXMeshDistort_1/useHemiLight/fixedModMix", 0],
			["bool_int", "/TXMeshDistort_1/useHemiLight/useExtMod", 0],
			["float", "/TXMeshDistort_1/useHemiLight/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorSaturation", [
			["float", "/TXMeshDistort_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorBrightness", [
			["float", "/TXMeshDistort_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/clearColorAlpha", [
			["float", "/TXMeshDistort_1/clearColorAlpha/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXMeshDistort_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/phase", [
			["float", "/TXMeshDistort_1/phase/fixedValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/phase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/phase/useExtMod", 0],
			["float", "/TXMeshDistort_1/phase/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/phase/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/phase/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/freqX", [
			["float", "/TXMeshDistort_1/freqX/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/freqX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/freqX/useExtMod", 0],
			["float", "/TXMeshDistort_1/freqX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/freqX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/freqX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/freqY", [
			["float", "/TXMeshDistort_1/freqY/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/freqY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/freqY/useExtMod", 0],
			["float", "/TXMeshDistort_1/freqY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/freqY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/freqY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/freqZ", [
			["float", "/TXMeshDistort_1/freqZ/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/freqZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/freqZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/freqZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/freqZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/freqZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/ampX", [
			["float", "/TXMeshDistort_1/ampX/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/ampX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/ampX/useExtMod", 0],
			["float", "/TXMeshDistort_1/ampX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/ampX/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/ampX/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/ampY", [
			["float", "/TXMeshDistort_1/ampY/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/ampY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/ampY/useExtMod", 0],
			["float", "/TXMeshDistort_1/ampY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/ampY/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/ampY/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/ampZ", [
			["float", "/TXMeshDistort_1/ampZ/fixedValue", 100, 0, 100],
			["float", "/TXMeshDistort_1/ampZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/ampZ/useExtMod", 0],
			["float", "/TXMeshDistort_1/ampZ/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/ampZ/softMin", 0, 0, 10000],
			["float", "/TXMeshDistort_1/ampZ/softMax", 100, 0, 10000],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/alpha", [
			["float", "/TXMeshDistort_1/alpha/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/alpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/alpha/useExtMod", 0],
			["float", "/TXMeshDistort_1/alpha/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/alpha/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/alpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawPosX", [
			["float", "/TXMeshDistort_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawPosX/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawPosY", [
			["float", "/TXMeshDistort_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawPosY/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawWidth", [
			["float", "/TXMeshDistort_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawWidth/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/drawHeight", [
			["float", "/TXMeshDistort_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/drawHeight/useExtMod", 0],
			["float", "/TXMeshDistort_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/anchorX", [
			["float", "/TXMeshDistort_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/anchorX/useExtMod", 0],
			["float", "/TXMeshDistort_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/anchorY", [
			["float", "/TXMeshDistort_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/anchorY/useExtMod", 0],
			["float", "/TXMeshDistort_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXMeshDistort_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/rotate", [
			["float", "/TXMeshDistort_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXMeshDistort_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/rotate/useExtMod", 0],
			["float", "/TXMeshDistort_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/rotate/softMin", -360, -360, 360],
			["float", "/TXMeshDistort_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/rotateMultiply", [
			["float", "/TXMeshDistort_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXMeshDistort_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/rotateMultiply/useExtMod", 0],
			["float", "/TXMeshDistort_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXMeshDistort_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/scaleX", [
			["float", "/TXMeshDistort_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXMeshDistort_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/scaleX/useExtMod", 0],
			["float", "/TXMeshDistort_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXMeshDistort_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXMeshDistort_1/scaleY", [
			["float", "/TXMeshDistort_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXMeshDistort_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXMeshDistort_1/scaleY/useExtMod", 0],
			["float", "/TXMeshDistort_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXMeshDistort_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXMeshDistort_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXMeshDistort_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXMeshDistort_1 ---------- */
		// empty
	];}

}


