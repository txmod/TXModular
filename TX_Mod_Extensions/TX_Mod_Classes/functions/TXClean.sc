// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXClean {		// removes bad values by outputting zeros

	// old
	// *ar{ arg input;
	// 	var good;
	// 	if (GUI.current.asSymbol == \SwingGUI, {
	// 		^input;
	// 		},{
	// 			good = BinaryOpUGen('==', CheckBadValues.ar(input, 0, 0), 0);
	// 			^Select.ar(good, [good, input]);
	// 	});
	//
	// }
	//
	// *kr{ arg input;
	// 	var good;
	// 	if (GUI.current.asSymbol == \SwingGUI, {
	// 		^input;
	// 		},{
	// 			good = BinaryOpUGen('==', CheckBadValues.kr(input, 0, 0), 0);
	// 			^Select.kr(good, [good, input]);
	// 	});
	// 	^input;
	// }

	*ar{ arg input;	^input.sanitize}

	*kr{ arg input;	^input.sanitize}

}
