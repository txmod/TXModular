// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawTransform3DMulti : TXV_Module {
	classvar <defaultName = "TXV Transf3D Multi";
	classvar <>arrInstances;
	var <>arrSlotData;

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."maxDepthRule":
	// , ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]

	// add text array to ... "transformOrder/fixedValue"
	// , ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawTransform3DMulti_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/drawActive", [
			["bool_int", "/TXDrawTransform3DMulti_1/drawActive/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/drawActive/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/drawActive/extModValue", 0, 0, 1],
		]],
		["int", "/TXDrawTransform3DMulti_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXDrawTransform3DMulti_1/maxDepthRule", 0, 0, 4, ["Use Screen Width", "Use Screen Height", "Use Max of Screen Width & Height", "Use Min of Screen Width & Height", "Use Custom Size", ]],
		["int", "/TXDrawTransform3DMulti_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXDrawTransform3DMulti_1/customMaxHeight", 768, 1, 4096],
		["int", "/TXDrawTransform3DMulti_1/customMaxDepth", 1024, 1, 4096],
		[ "modParameterGroupInt", "/TXDrawTransform3DMulti_1/drawLayersRule", [
			["int", "/TXDrawTransform3DMulti_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXDrawTransform3DMulti_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/drawLayersRule/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform3DMulti_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXDrawTransform3DMulti_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupInt", "/TXDrawTransform3DMulti_1/numCopies", [
			["int", "/TXDrawTransform3DMulti_1/numCopies/fixedValue", 4, 1, 50],
			["float", "/TXDrawTransform3DMulti_1/numCopies/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/numCopies/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/numCopies/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform3DMulti_1/numCopies/softMin", 1, 0, 1000],
			["int", "/TXDrawTransform3DMulti_1/numCopies/softMax", 50, 0, 1000],
		]],
		[ "modParameterGroupInt", "/TXDrawTransform3DMulti_1/transformOrder", [
			["int", "/TXDrawTransform3DMulti_1/transformOrder/fixedValue", 0, 0, 5, ["Shift Rotate Scale", "Shift Scale Rotate", "Rotate Scale Shift", "Rotate Shift Scale", "Scale Shift Rotate", "Scale Rotate Shift"]],
			["float", "/TXDrawTransform3DMulti_1/transformOrder/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/transformOrder/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/transformOrder/extModValue", 0, 0, 1],
			["int", "/TXDrawTransform3DMulti_1/transformOrder/softMin", 0, 0, 5],
			["int", "/TXDrawTransform3DMulti_1/transformOrder/softMax", 5, 0, 5],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftX", [
			["float", "/TXDrawTransform3DMulti_1/shiftX/fixedValue", -0.25, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftX/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftX/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftXEnd", [
			["float", "/TXDrawTransform3DMulti_1/shiftXEnd/fixedValue", 0.25, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXEnd/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftXEnd/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/shiftXEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXEndIgnore/fixedValue", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftXEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftY", [
			["float", "/TXDrawTransform3DMulti_1/shiftY/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftY/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftY/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftYEnd", [
			["float", "/TXDrawTransform3DMulti_1/shiftYEnd/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYEnd/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftYEnd/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/shiftYEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftYEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftZ", [
			["float", "/TXDrawTransform3DMulti_1/shiftZ/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZ/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZ/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftZ/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftZEnd", [
			["float", "/TXDrawTransform3DMulti_1/shiftZEnd/fixedValue", 0, -1, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftZEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZEnd/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/shiftZEnd/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/shiftZEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftZEndIgnore/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/shiftXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/shiftXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/shiftYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/shiftYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/shiftZSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/shiftZSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/shiftZSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawTransform3DMulti_1/useShiftXSpreadCurveForShiftY", 0],
		["bool_int", "/TXDrawTransform3DMulti_1/useShiftXSpreadCurveForShiftZ", 0],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateX", [
			["float", "/TXDrawTransform3DMulti_1/rotateX/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateX/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateX/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateX/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateXEnd", [
			["float", "/TXDrawTransform3DMulti_1/rotateXEnd/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXEnd/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateXEnd/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/rotateXEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateXEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateY", [
			["float", "/TXDrawTransform3DMulti_1/rotateY/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateY/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateY/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateY/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateYEnd", [
			["float", "/TXDrawTransform3DMulti_1/rotateYEnd/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYEnd/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateYEnd/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/rotateYEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateYEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateZ", [
			["float", "/TXDrawTransform3DMulti_1/rotateZ/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZ/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZ/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateZ/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateZEnd", [
			["float", "/TXDrawTransform3DMulti_1/rotateZEnd/fixedValue", 0, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateZEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateZEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZEnd/softMin", -360, -360, 360],
			["float", "/TXDrawTransform3DMulti_1/rotateZEnd/softMax", 360, -360, 360],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/rotateZEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateZEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateMultiply", [
			["float", "/TXDrawTransform3DMulti_1/rotateMultiply/fixedValue", 1, 0, 10],
			["float", "/TXDrawTransform3DMulti_1/rotateMultiply/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateMultiply/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateMultiply/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateMultiply/softMin", 0, 0, 10],
			["float", "/TXDrawTransform3DMulti_1/rotateMultiply/softMax", 10, 0, 10],
		]],
		["float256", "/TXDrawTransform3DMulti_1/rotateXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/rotateXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/rotateYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/rotateYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/rotateZSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/rotateZSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/rotateZSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawTransform3DMulti_1/useRotateXSpreadCurveForRotateY", 0],
		["bool_int", "/TXDrawTransform3DMulti_1/useRotateXSpreadCurveForRotateZ", 0],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorX", [
			["float", "/TXDrawTransform3DMulti_1/anchorX/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorX/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorX/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorX/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorXEnd", [
			["float", "/TXDrawTransform3DMulti_1/anchorXEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXEnd/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorXEnd/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/anchorXEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorXEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorY", [
			["float", "/TXDrawTransform3DMulti_1/anchorY/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorY/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorY/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorY/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorYEnd", [
			["float", "/TXDrawTransform3DMulti_1/anchorYEnd/fixedValue", 0.5, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYEnd/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorYEnd/softMax", 1, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/anchorYEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorYEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorZ", [
			["float", "/TXDrawTransform3DMulti_1/anchorZ/fixedValue", 0, -1, 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZ/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZ/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorZ/softMax", 0, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorZEnd", [
			["float", "/TXDrawTransform3DMulti_1/anchorZEnd/fixedValue", 0, -1, 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZEnd/softMin", -1, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/anchorZEnd/softMax", 0, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/anchorZEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZEndIgnore/extModValue", 0, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/anchorXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/anchorXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/anchorYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/anchorYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/anchorZSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/anchorZSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/anchorZSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawTransform3DMulti_1/useAnchorXSpreadCurveForAnchorY", 0],
		["bool_int", "/TXDrawTransform3DMulti_1/useAnchorXSpreadCurveForAnchorZ", 0],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleX", [
			["float", "/TXDrawTransform3DMulti_1/scaleX/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleX/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleX/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleX/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleX/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleX/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleXEnd", [
			["float", "/TXDrawTransform3DMulti_1/scaleXEnd/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleXEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleXEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXEnd/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleXEnd/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/scaleXEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleXEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleY", [
			["float", "/TXDrawTransform3DMulti_1/scaleY/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleY/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleY/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleY/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleY/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleY/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleYEnd", [
			["float", "/TXDrawTransform3DMulti_1/scaleYEnd/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleYEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleYEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYEnd/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleYEnd/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/scaleYEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleYEndIgnore/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleZ", [
			["float", "/TXDrawTransform3DMulti_1/scaleZ/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleZ/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZ/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleZ/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZ/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleZ/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleZEnd", [
			["float", "/TXDrawTransform3DMulti_1/scaleZEnd/fixedValue", 1, 0, 3],
			["float", "/TXDrawTransform3DMulti_1/scaleZEnd/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZEnd/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleZEnd/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZEnd/softMin", 0, -10, 10],
			["float", "/TXDrawTransform3DMulti_1/scaleZEnd/softMax", 3, -10, 10],
		]],
		[ "modParameterGroupBool", "/TXDrawTransform3DMulti_1/scaleZEndIgnore", [
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZEndIgnore/fixedValue", 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZEndIgnore/fixedModMix", 0],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZEndIgnore/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleZEndIgnore/extModValue", 0, 0, 1],
		]],
		["bool_int", "/TXDrawTransform3DMulti_1/useScaleXForScaleY", 0],
		["bool_int", "/TXDrawTransform3DMulti_1/useScaleXForScaleZ", 0],
		["float256", "/TXDrawTransform3DMulti_1/scaleXSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/scaleXSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleXSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/scaleYSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/scaleYSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleYSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["float256", "/TXDrawTransform3DMulti_1/scaleZSpreadCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXDrawTransform3DMulti_1/scaleZSpreadCurve2", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph", [
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurveMorph/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase", [
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/fixedValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/fixedModMix", 0, 0, 1],
			["bool_int", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/useExtMod", 0],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/extModValue", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/softMin", 0, 0, 1],
			["float", "/TXDrawTransform3DMulti_1/scaleZSpreadCurvePhase/softMax", 1, 0, 1],
		]],
		["bool_int", "/TXDrawTransform3DMulti_1/useScaleXSpreadCurveForScaleY", 0],
		["bool_int", "/TXDrawTransform3DMulti_1/useScaleXSpreadCurveForScaleZ", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXDrawTransform3DMulti_1 ---------- */
		// empty
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Transform & Shift", {arg parameterName;
				(parameterName.containsi("shift")
					|| parameterName.containsi("transformOrder"))
				&& (parameterName.containsi("curve") == false)
				;}],
			["Shift Spread", {arg parameterName;
				(parameterName.containsi("shift")
					|| parameterName.containsi("transformOrder"))
				&& parameterName.containsi("curve")
				;}],
			["Scale", {arg parameterName;
				parameterName.containsi("scale")
				&& (parameterName.containsi("curve") == false)
				;}],
			["Scale Spread", {arg parameterName;
				parameterName.containsi("scale")
				&& parameterName.containsi("curve")
				;}],
			["Anchor", {arg parameterName;
				parameterName.containsi("anchor")
				&& (parameterName.containsi("curve") == false)
				;}],
			["Anchor Spread", {arg parameterName;
				parameterName.containsi("anchor")
				&& parameterName.containsi("curve")
				;}],
			["Rotate", {arg parameterName;
				parameterName.containsi("rotate")
				&& (parameterName.containsi("curve") == false)
				;}],
			["Rotate Spread", {arg parameterName;
				parameterName.containsi("rotate")
				&& parameterName.containsi("curve")
				;}],
			["Draw Parameters", {arg parameterName;
				var drawParameterNames = ["drawActive", "maxWidthHeightRule", "maxDepthRule",
					"customMaxWidth", "customMaxHeight", "customMaxDepth", "drawLayersRule",];
				// return validity bool
				drawParameterNames.indexOfEqual(parameterName).notNil;
			}],
		];
	}

	// override
	getDefaultParamSectionName {  // override
		^"Copies";
	}


	initExtraActions { // override
		arrSlotData = Array.newClear(256).seriesFill(0, 1/255).dup(5); // init to ramp
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


