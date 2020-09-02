// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// add text for "handMode/fixedValue"
// , ["2 hands, separate spaces", "2 hands, shared space", "1 hand"]

TXV_LeapMotion : TXV_Module {
	classvar <defaultName = "TXV Leap Motion";
	classvar <>arrInstances;
	var <>arrSlotData;

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXLeapMotion_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXLeapMotion_1/active", [
			["bool_int", "/TXLeapMotion_1/active/fixedValue", 1],
			["bool_int", "/TXLeapMotion_1/active/fixedModMix", 0],
			["bool_int", "/TXLeapMotion_1/active/useExtMod", 0],
			["float", "/TXLeapMotion_1/active/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupInt", "/TXLeapMotion_1/handMode", [
			["int", "/TXLeapMotion_1/handMode/fixedValue", 0, 0, 2, ["2 hands, separate spaces", "2 hands, shared space", "1 hand"]],
			["float", "/TXLeapMotion_1/handMode/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLeapMotion_1/handMode/useExtMod", 0],
			["float", "/TXLeapMotion_1/handMode/extModValue", 0, 0, 1],
			["int", "/TXLeapMotion_1/handMode/softMin", 0, 0, 2],
			["int", "/TXLeapMotion_1/handMode/softMax", 2, 0, 2],
		]],
		[ "modParameterGroupBool", "/TXLeapMotion_1/swapHands", [
			["bool_int", "/TXLeapMotion_1/swapHands/fixedValue", 0],
			["bool_int", "/TXLeapMotion_1/swapHands/fixedModMix", 0],
			["bool_int", "/TXLeapMotion_1/swapHands/useExtMod", 0],
			["float", "/TXLeapMotion_1/swapHands/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXLeapMotion_1/getFingerData", [
			["bool_int", "/TXLeapMotion_1/getFingerData/fixedValue", 0],
			["bool_int", "/TXLeapMotion_1/getFingerData/fixedModMix", 0],
			["bool_int", "/TXLeapMotion_1/getFingerData/useExtMod", 0],
			["float", "/TXLeapMotion_1/getFingerData/extModValue", 0, 0, 1],
		]],
		// [ "modParameterGroupFloat", "/TXLeapMotion_1/detectionConfidence", [
		// 	["float", "/TXLeapMotion_1/detectionConfidence/fixedValue", 0.7, 0, 1],
		// 	["float", "/TXLeapMotion_1/detectionConfidence/fixedModMix", 0, 0, 1],
		// 	["bool_int", "/TXLeapMotion_1/detectionConfidence/useExtMod", 0],
		// 	["float", "/TXLeapMotion_1/detectionConfidence/extModValue", 0, 0, 1],
		// 	["float", "/TXLeapMotion_1/detectionConfidence/softMin", 0, 0, 1],
		// 	["float", "/TXLeapMotion_1/detectionConfidence/softMax", 1, 0, 1],
		// ]],
		[ "modParameterGroupBool", "/TXLeapMotion_1/stabilizePalmPosition", [
			["bool_int", "/TXLeapMotion_1/stabilizePalmPosition/fixedValue", 1],
			["bool_int", "/TXLeapMotion_1/stabilizePalmPosition/fixedModMix", 0],
			["bool_int", "/TXLeapMotion_1/stabilizePalmPosition/useExtMod", 0],
			["float", "/TXLeapMotion_1/stabilizePalmPosition/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/positionSmoothTime", [
			["float", "/TXLeapMotion_1/positionSmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXLeapMotion_1/positionSmoothTime/fixedModMix", 0, 0, 2],
			["bool_int", "/TXLeapMotion_1/positionSmoothTime/useExtMod", 0],
			["float", "/TXLeapMotion_1/positionSmoothTime/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/positionSmoothTime/softMin", 0, 0, 10],
			["float", "/TXLeapMotion_1/positionSmoothTime/softMax", 2, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/rotationSmoothTime", [
			["float", "/TXLeapMotion_1/rotationSmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXLeapMotion_1/rotationSmoothTime/fixedModMix", 0, 0, 2],
			["bool_int", "/TXLeapMotion_1/rotationSmoothTime/useExtMod", 0],
			["float", "/TXLeapMotion_1/rotationSmoothTime/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/rotationSmoothTime/softMin", 0, 0, 10],
			["float", "/TXLeapMotion_1/rotationSmoothTime/softMax", 2, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/velocitySmoothTime", [
			["float", "/TXLeapMotion_1/velocitySmoothTime/fixedValue", 0, 0, 2],
			["float", "/TXLeapMotion_1/velocitySmoothTime/fixedModMix", 0, 0, 2],
			["bool_int", "/TXLeapMotion_1/velocitySmoothTime/useExtMod", 0],
			["float", "/TXLeapMotion_1/velocitySmoothTime/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/velocitySmoothTime/softMin", 0, 0, 10],
			["float", "/TXLeapMotion_1/velocitySmoothTime/softMax", 2, 0, 10],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/velocityScaling", [
			["float", "/TXLeapMotion_1/velocityScaling/fixedValue", 1, 0.1, 1],
			["float", "/TXLeapMotion_1/velocityScaling/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLeapMotion_1/velocityScaling/useExtMod", 0],
			["float", "/TXLeapMotion_1/velocityScaling/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/velocityScaling/softMin", 0.1, 0.01, 10],
			["float", "/TXLeapMotion_1/velocityScaling/softMax", 1, 0.01, 10],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/palmPositionCentreBias", [
			["float", "/TXLeapMotion_1/palmPositionCentreBias/fixedValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmPositionCentreBias/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLeapMotion_1/palmPositionCentreBias/useExtMod", 0],
			["float", "/TXLeapMotion_1/palmPositionCentreBias/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmPositionCentreBias/softMin", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmPositionCentreBias/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXLeapMotion_1/palmRotationCentreBias", [
			["float", "/TXLeapMotion_1/palmRotationCentreBias/fixedValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmRotationCentreBias/fixedModMix", 0, 0, 1],
			["bool_int", "/TXLeapMotion_1/palmRotationCentreBias/useExtMod", 0],
			["float", "/TXLeapMotion_1/palmRotationCentreBias/extModValue", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmRotationCentreBias/softMin", 0, 0, 1],
			["float", "/TXLeapMotion_1/palmRotationCentreBias/softMax", 1, 0, 1],
		]],
		["float256", "/TXLeapMotion_1/grabCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],
		["float256", "/TXLeapMotion_1/pinchCurve", 0, 0.00392157, 0.00784314, 0.0117647, 0.0156863, 0.0196078, 0.0235294, 0.027451, 0.0313726, 0.0352941, 0.0392157, 0.0431373, 0.0470588, 0.0509804, 0.054902, 0.0588235, 0.0627451, 0.0666667, 0.0705882, 0.0745098, 0.0784314, 0.0823529, 0.0862745, 0.0901961, 0.0941176, 0.0980392, 0.101961, 0.105882, 0.109804, 0.113725, 0.117647, 0.121569, 0.12549, 0.129412, 0.133333, 0.137255, 0.141176, 0.145098, 0.14902, 0.152941, 0.156863, 0.160784, 0.164706, 0.168627, 0.172549, 0.176471, 0.180392, 0.184314, 0.188235, 0.192157, 0.196078, 0.2, 0.203922, 0.207843, 0.211765, 0.215686, 0.219608, 0.223529, 0.227451, 0.231373, 0.235294, 0.239216, 0.243137, 0.247059, 0.25098, 0.254902, 0.258824, 0.262745, 0.266667, 0.270588, 0.27451, 0.278431, 0.282353, 0.286275, 0.290196, 0.294118, 0.298039, 0.301961, 0.305882, 0.309804, 0.313726, 0.317647, 0.321569, 0.32549, 0.329412, 0.333333, 0.337255, 0.341176, 0.345098, 0.34902, 0.352941, 0.356863, 0.360784, 0.364706, 0.368627, 0.372549, 0.376471, 0.380392, 0.384314, 0.388235, 0.392157, 0.396078, 0.4, 0.403922, 0.407843, 0.411765, 0.415686, 0.419608, 0.423529, 0.427451, 0.431373, 0.435294, 0.439216, 0.443137, 0.447059, 0.45098, 0.454902, 0.458824, 0.462745, 0.466667, 0.470588, 0.47451, 0.478431, 0.482353, 0.486275, 0.490196, 0.494118, 0.498039, 0.501961, 0.505882, 0.509804, 0.513726, 0.517647, 0.521569, 0.52549, 0.529412, 0.533333, 0.537255, 0.541176, 0.545098, 0.54902, 0.552941, 0.556863, 0.560784, 0.564706, 0.568627, 0.572549, 0.576471, 0.580392, 0.584314, 0.588235, 0.592157, 0.596078, 0.6, 0.603922, 0.607843, 0.611765, 0.615686, 0.619608, 0.623529, 0.627451, 0.631373, 0.635294, 0.639216, 0.643137, 0.647059, 0.65098, 0.654902, 0.658824, 0.662745, 0.666667, 0.670588, 0.67451, 0.678431, 0.682353, 0.686275, 0.690196, 0.694118, 0.698039, 0.701961, 0.705882, 0.709804, 0.713726, 0.717647, 0.721569, 0.72549, 0.729412, 0.733333, 0.737255, 0.741176, 0.745098, 0.74902, 0.752941, 0.756863, 0.760784, 0.764706, 0.768627, 0.772549, 0.776471, 0.780392, 0.784314, 0.788235, 0.792157, 0.796078, 0.8, 0.803922, 0.807843, 0.811765, 0.815686, 0.819608, 0.823529, 0.827451, 0.831373, 0.835294, 0.839216, 0.843137, 0.847059, 0.85098, 0.854902, 0.858824, 0.862745, 0.866667, 0.870588, 0.87451, 0.878431, 0.882353, 0.886275, 0.890196, 0.894118, 0.898039, 0.901961, 0.905882, 0.909804, 0.913725, 0.917647, 0.921569, 0.92549, 0.929412, 0.933333, 0.937255, 0.941176, 0.945098, 0.94902, 0.952941, 0.956863, 0.960784, 0.964706, 0.968627, 0.972549, 0.976471, 0.980392, 0.984314, 0.988235, 0.992157, 0.996078, 1, ],		["int", "/TXLeapMotion_1/XAxisMinMM", -250, -1000, 1000],
		["int", "/TXLeapMotion_1/XAxisMaxMM", 250, -1000, 1000],
		["int", "/TXLeapMotion_1/YAxisMinMM", 200, 90, 1000],
		["int", "/TXLeapMotion_1/YAxisMaxMM", 500, 90, 1000],
		["int", "/TXLeapMotion_1/ZAxisMinMM", -150, -1000, 1000],
		["int", "/TXLeapMotion_1/ZAxisMaxMM", 150, -1000, 1000],
		["bool_int", "/TXLeapMotion_1/sendLeftHandDataToSC", 0],
		["bool_int", "/TXLeapMotion_1/sendRightHandDataToSC", 0],
		["bool_int", "/TXLeapMotion_1/sendPositionToSC", 1],
		["bool_int", "/TXLeapMotion_1/sendPinchGrabToSC", 1],
		["bool_int", "/TXLeapMotion_1/sendRotationToSC", 1],
		["bool_int", "/TXLeapMotion_1/sendVelocityToSC", 0],
		["bool_int", "/TXLeapMotion_1/sendFingerDataToSC", 0],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXLeapMotion_1 ---------- */
		"leftHandFingersExtended0",
		"leftHandFingersExtended1",
		"leftHandFingersExtended2",
		"leftHandFingersExtended3",
		"leftHandFingersExtended4",
		"leftHandFingersExtended5",
		"leftHandFound",
		"leftHandGrab",
		"leftHandIndexFingerExtend",
		"leftHandIndexFingerPosX",
		"leftHandIndexFingerPosY",
		"leftHandIndexFingerPosZ",
		"leftHandMiddleFingerExtend",
		"leftHandMiddleFingerPosX",
		"leftHandMiddleFingerPosY",
		"leftHandMiddleFingerPosZ",
		"leftHandPalmPosX",
		"leftHandPalmPosY",
		"leftHandPalmPosZ",
		"leftHandPalmRotX",
		"leftHandPalmRotY",
		"leftHandPalmRotZ",
		"leftHandPalmVelocityX",
		"leftHandPalmVelocityY",
		"leftHandPalmVelocityZ",
		"leftHandPinch",
		"leftHandPinkyFingerExtend",
		"leftHandPinkyFingerPosX",
		"leftHandPinkyFingerPosY",
		"leftHandPinkyFingerPosZ",
		"leftHandRingFingerExtend",
		"leftHandRingFingerPosX",
		"leftHandRingFingerPosY",
		"leftHandRingFingerPosZ",
		"leftHandThumbExtend",
		"leftHandThumbPosX",
		"leftHandThumbPosY",
		"leftHandThumbPosZ",
		"rightHandFingersExtended0",
		"rightHandFingersExtended1",
		"rightHandFingersExtended2",
		"rightHandFingersExtended3",
		"rightHandFingersExtended4",
		"rightHandFingersExtended5",
		"rightHandFound",
		"rightHandGrab",
		"rightHandIndexFingerExtend",
		"rightHandIndexFingerPosX",
		"rightHandIndexFingerPosY",
		"rightHandIndexFingerPosZ",
		"rightHandMiddleFingerExtend",
		"rightHandMiddleFingerPosX",
		"rightHandMiddleFingerPosY",
		"rightHandMiddleFingerPosZ",
		"rightHandPalmPosX",
		"rightHandPalmPosY",
		"rightHandPalmPosZ",
		"rightHandPalmRotX",
		"rightHandPalmRotY",
		"rightHandPalmRotZ",
		"rightHandPalmVelocityX",
		"rightHandPalmVelocityY",
		"rightHandPalmVelocityZ",
		"rightHandPinch",
		"rightHandPinkyFingerExtend",
		"rightHandPinkyFingerPosX",
		"rightHandPinkyFingerPosY",
		"rightHandPinkyFingerPosZ",
		"rightHandRingFingerExtend",
		"rightHandRingFingerPosX",
		"rightHandRingFingerPosY",
		"rightHandRingFingerPosZ",
		"rightHandThumbExtend",
		"rightHandThumbPosX",
		"rightHandThumbPosY",
		"rightHandThumbPosZ",
	];}

	getTXVParameterSectionSpecs {
		^[
			// format: ["Example Name", nameValidityFunction],
			["Curves", {arg parameterName;
				parameterName.containsi("curve");
			}],
			["Smoothing", {arg parameterName;
				parameterName.containsi("Smooth");
			}],
			["Interaction Box", {arg parameterName;
				parameterName.containsi("Axis");
			}],
			["Send OSC to SC", {arg parameterName;
				parameterName.containsi("ToSC");
			}],
		];
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


