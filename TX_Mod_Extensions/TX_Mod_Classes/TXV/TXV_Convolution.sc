// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Convolution : TXV_Module {
	classvar <defaultName = "TXV Convolution";
	classvar <>arrInstances;

	// add text array to  ..."ScaleMode/fixedValue":
	// , ["scale to fit", "scale & crop to fill", "stretch to fill"]

	// add text array to ..."renderWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// enum TXKERNELTYPE {TXKERNELTYPE_BLUR1, //
	// 	TXKERNELTYPE_BLUR2, //
	// 	TXKERNELTYPE_FINDEDGE1, //
	// 	TXKERNELTYPE_FINDEDGE2, //
	// 	TXKERNELTYPE_FINDVERTICALEDGE1, //
	// 	TXKERNELTYPE_FINDVERTICALEDGE2, //
	// 	TXKERNELTYPE_FINDHORIZONTALEDGE1, //
	// 	TXKERNELTYPE_FINDHORIZONTALEDGE2, //
	// 	TXKERNELTYPE_SHARPEN1, //
	// 	TXKERNELTYPE_SHARPEN2, // - USES 5 IN BETWEEN OTHERS
	// 	TXKERNELTYPE_SHARPEN3, //
	// 	TXKERNELTYPE_EMBOSS1, // {-2, -2, 0, -2, 6, 0, 0, 0, 0}
	// 	TXKERNELTYPE_EMBOSS2, // {-2, -1, 0, -1, 1, 1, 0, 1, 2}
	// 	TXKERNELTYPE_CUSTOMISE, // USE K0-K8

	// add text array to ..."kernelType/fixedValue":
	// , ["Blur 1", "Blur 2", "Find Edge 1", "Find Edge 2", "Find Vertical Edge 1", "Find Vertical Edge 2", "Find Horizontal Edge 1", "Find Horizontal Edge 2", "Sharpen 1", "Sharpen 2", "Sharpen 3", "Emboss 1", "Emboss 2", "Customise (use kernel1 - kernel9)", ]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXConvolution_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["assetSlot/image", "assetSlot/image/sourceImageAsset", 0],
		["extImageModuleSlot", "extImageModuleSlot/extSourceImageModule", 0],
		[ "modParameterGroupBool", "/TXConvolution_1/drawActive", [
			["bool_int", "/TXConvolution_1/drawActive/fixedValue", 1],
			["bool_int", "/TXConvolution_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXConvolution_1/drawActive/useExtMod", 0],
			["float", "/TXConvolution_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXConvolution_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXConvolution_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXConvolution_1/customMaxHeight", 768, 1, 4096],
		//["int", "/TXConvolution_1/customMaxDepth", 1024, 1, 4096],
		["bool_int", "/TXConvolution_1/useExternalSourceImage", 0],
		[ "modParameterGroupInt", "/TXConvolution_1/sourceImageScaleMode", [
			["int", "/TXConvolution_1/sourceImageScaleMode/fixedValue", 0, 0, 2, ["scale to fit", "scale & crop to fill", "stretch to fill"]],
			["float", "/TXConvolution_1/sourceImageScaleMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/sourceImageScaleMode/useExtMod", 0],
			["float", "/TXConvolution_1/sourceImageScaleMode/extModValue", 0, 0, 1],
			["int", "/TXConvolution_1/sourceImageScaleMode/softMin", 0, 0, 2],
			["int", "/TXConvolution_1/sourceImageScaleMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXConvolution_1/renderContinuosly", [
			["bool_int", "/TXConvolution_1/renderContinuosly/fixedValue", 1],
			["bool_int", "/TXConvolution_1/renderContinuosly/fixedModMix", 0],
			["bool_int", "/TXConvolution_1/renderContinuosly/useExtMod", 0],
			["float", "/TXConvolution_1/renderContinuosly/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXConvolution_1/renderNow", [
			["bool_int", "/TXConvolution_1/renderNow/fixedValue", 0],
			["bool_int", "/TXConvolution_1/renderNow/fixedModMix", 0],
			["bool_int", "/TXConvolution_1/renderNow/useExtMod", 0],
			["float", "/TXConvolution_1/renderNow/extModValue", 0, 0, 1],
		]],
		["int", "/TXConvolution_1/renderWidthHeightRule", 0, 0, 6, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", "Use Source Image Size", ]],
		["int", "/TXConvolution_1/renderCustomWidth", 1024, 1, 4096],
		["int", "/TXConvolution_1/renderCustomHeight", 768, 1, 4096],
		[ "modParameterGroupBool", "/TXConvolution_1/clearBeforeRender", [
			["bool_int", "/TXConvolution_1/clearBeforeRender/fixedValue", 1],
			["bool_int", "/TXConvolution_1/clearBeforeRender/fixedModMix", 0],
			["bool_int", "/TXConvolution_1/clearBeforeRender/useExtMod", 0],
			["float", "/TXConvolution_1/clearBeforeRender/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXConvolution_1/clearNow", [
			["bool_int", "/TXConvolution_1/clearNow/fixedValue", 0],
			["bool_int", "/TXConvolution_1/clearNow/fixedModMix", 0],
			["bool_int", "/TXConvolution_1/clearNow/useExtMod", 0],
			["float", "/TXConvolution_1/clearNow/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/clearColorHue", [
			["float", "/TXConvolution_1/clearColorHue/fixedValue", 0.5, 0, 1],
			["float", "/TXConvolution_1/clearColorHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/clearColorHue/useExtMod", 0],
			["float", "/TXConvolution_1/clearColorHue/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorHue/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/clearColorSaturation", [
			["float", "/TXConvolution_1/clearColorSaturation/fixedValue", 0.6, 0, 1],
			["float", "/TXConvolution_1/clearColorSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/clearColorSaturation/useExtMod", 0],
			["float", "/TXConvolution_1/clearColorSaturation/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorSaturation/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/clearColorBrightness", [
			["float", "/TXConvolution_1/clearColorBrightness/fixedValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/clearColorBrightness/useExtMod", 0],
			["float", "/TXConvolution_1/clearColorBrightness/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorBrightness/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/clearColorAlpha", [
			["float", "/TXConvolution_1/clearColorAlpha/fixedValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/clearColorAlpha/useExtMod", 0],
			["float", "/TXConvolution_1/clearColorAlpha/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorAlpha/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/clearColorAlpha/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXConvolution_1/clearColorHSBAsRGB", 0],
		[ "modParameterGroupFloat", "/TXConvolution_1/mix", [
			["float", "/TXConvolution_1/mix/fixedValue", 1, 0, 1],
			["float", "/TXConvolution_1/mix/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/mix/useExtMod", 0],
			["float", "/TXConvolution_1/mix/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/mix/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/mix/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXConvolution_1/kernelType", [
			["int", "/TXConvolution_1/kernelType/fixedValue", 0, 0, 13, ["Blur 1", "Blur 2", "Find Edge 1", "Find Edge 2", "Find Vertical Edge 1", "Find Vertical Edge 2", "Find Horizontal Edge 1", "Find Horizontal Edge 2", "Sharpen 1", "Sharpen 2", "Sharpen 3", "Emboss 1", "Emboss 2", "Customise (use kernel1 - kernel9)", ]],
			["float", "/TXConvolution_1/kernelType/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernelType/useExtMod", 0],
			["float", "/TXConvolution_1/kernelType/extModValue", 0, 0, 1],
			["int", "/TXConvolution_1/kernelType/softMin", 0, 0, 13],
			["int", "/TXConvolution_1/kernelType/softMax", 13, 0, 13],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/bias", [
			["float", "/TXConvolution_1/bias/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/bias/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/bias/useExtMod", 0],
			["float", "/TXConvolution_1/bias/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/bias/softMin", -1, -128, 128],
			["float", "/TXConvolution_1/bias/softMax", 1, -128, 128],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel1", [
			["float", "/TXConvolution_1/kernel1/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel1/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel1/useExtMod", 0],
			["float", "/TXConvolution_1/kernel1/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel1/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel1/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel2", [
			["float", "/TXConvolution_1/kernel2/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel2/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel2/useExtMod", 0],
			["float", "/TXConvolution_1/kernel2/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel2/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel2/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel3", [
			["float", "/TXConvolution_1/kernel3/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel3/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel3/useExtMod", 0],
			["float", "/TXConvolution_1/kernel3/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel3/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel3/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel4", [
			["float", "/TXConvolution_1/kernel4/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel4/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel4/useExtMod", 0],
			["float", "/TXConvolution_1/kernel4/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel4/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel4/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel5", [
			["float", "/TXConvolution_1/kernel5/fixedValue", 1, -10, 10],
			["float", "/TXConvolution_1/kernel5/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel5/useExtMod", 0],
			["float", "/TXConvolution_1/kernel5/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel5/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel5/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel6", [
			["float", "/TXConvolution_1/kernel6/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel6/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel6/useExtMod", 0],
			["float", "/TXConvolution_1/kernel6/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel6/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel6/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel7", [
			["float", "/TXConvolution_1/kernel7/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel7/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel7/useExtMod", 0],
			["float", "/TXConvolution_1/kernel7/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel7/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel7/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel8", [
			["float", "/TXConvolution_1/kernel8/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel8/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel8/useExtMod", 0],
			["float", "/TXConvolution_1/kernel8/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel8/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel8/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/kernel9", [
			["float", "/TXConvolution_1/kernel9/fixedValue", 0, -10, 10],
			["float", "/TXConvolution_1/kernel9/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/kernel9/useExtMod", 0],
			["float", "/TXConvolution_1/kernel9/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/kernel9/softMin", -10, -30, 30],
			["float", "/TXConvolution_1/kernel9/softMax", 10, -30, 30],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/drawAlpha", [
			["float", "/TXConvolution_1/drawAlpha/fixedValue", 1, 0, 1],
			["float", "/TXConvolution_1/drawAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/drawAlpha/useExtMod", 0],
			["float", "/TXConvolution_1/drawAlpha/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/drawAlpha/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/drawAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/drawPosX", [
			["float", "/TXConvolution_1/drawPosX/fixedValue", 0.5, 0, 1],
			["float", "/TXConvolution_1/drawPosX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/drawPosX/useExtMod", 0],
			["float", "/TXConvolution_1/drawPosX/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/drawPosX/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/drawPosX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/drawPosY", [
			["float", "/TXConvolution_1/drawPosY/fixedValue", 0.5, 0, 1],
			["float", "/TXConvolution_1/drawPosY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/drawPosY/useExtMod", 0],
			["float", "/TXConvolution_1/drawPosY/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/drawPosY/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/drawPosY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/drawWidth", [
			["float", "/TXConvolution_1/drawWidth/fixedValue", 1, 0, 1],
			["float", "/TXConvolution_1/drawWidth/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/drawWidth/useExtMod", 0],
			["float", "/TXConvolution_1/drawWidth/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/drawWidth/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/drawWidth/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/drawHeight", [
			["float", "/TXConvolution_1/drawHeight/fixedValue", 1, 0, 1],
			["float", "/TXConvolution_1/drawHeight/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/drawHeight/useExtMod", 0],
			["float", "/TXConvolution_1/drawHeight/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/drawHeight/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/drawHeight/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/anchorX", [
			["float", "/TXConvolution_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXConvolution_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/anchorX/useExtMod", 0],
			["float", "/TXConvolution_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/anchorX/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/anchorX/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/anchorY", [
			["float", "/TXConvolution_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXConvolution_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/anchorY/useExtMod", 0],
			["float", "/TXConvolution_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/anchorY/softMin", 0, 0, 1],
			["float", "/TXConvolution_1/anchorY/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/rotate", [
			["float", "/TXConvolution_1/rotate/fixedValue", 0, -360, 360],
			["float", "/TXConvolution_1/rotate/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/rotate/useExtMod", 0],
			["float", "/TXConvolution_1/rotate/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/rotate/softMin", -360, -360, 360],
			["float", "/TXConvolution_1/rotate/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/rotateMultiply", [
			["float", "/TXConvolution_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXConvolution_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/rotateMultiply/useExtMod", 0],
			["float", "/TXConvolution_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXConvolution_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/scaleX", [
			["float", "/TXConvolution_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXConvolution_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/scaleX/useExtMod", 0],
			["float", "/TXConvolution_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXConvolution_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXConvolution_1/scaleY", [
			["float", "/TXConvolution_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXConvolution_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXConvolution_1/scaleY/useExtMod", 0],
			["float", "/TXConvolution_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXConvolution_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXConvolution_1/scaleY/softMax", 3, -10, 10],
		]],
		["bool_int", "/TXConvolution_1/useScaleXForScaleY", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXConvolution_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Convolution", {arg parameterName;
				var arrNames = ["mix", "kernelType", "bias", "kernel1", "kernel2", "kernel3", "kernel4", "kernel5",
					"kernel6", "kernel7", "kernel8", "kernel9", ];
				arrNames.indexOfEqual(parameterName).notNil;
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
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "anchorX", "anchorY",
					"rotate", "rotateX", "rotateY", "rotateZ", "rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	getDefaultParamSectionName {  // override
		^"Image";
	}

}


