// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXListView : ListView {	// modified ListView

	// this version reverts the changes in ListViewto mouseDownEvent & mouseMoveEvent because they can cause crashes

	// ListView version
	// mouseDownEvent { arg x, y, modifiers, buttonNumber, clickCount;
	// 	// Override View:mouseDownEvent: postpone drag start to move event
	// 	modifiers = QKeyModifiers.toCocoa(modifiers);
	// 	^this.mouseDown( x, y, modifiers, buttonNumber, clickCount );
	// }
	//
	// mouseMoveEvent { arg x, y, modifiers, buttons;
	// 	// Override View:mouseMoveEvent: start drag
	// 	if( buttons != 0 and: ((modifiers & QKeyModifiers.control) > 0) ) {
	// 		if( this.beginDrag( x, y ) ) { ^true };
	// 	};
	//
	// 	^super.mouseMoveEvent(x, y, modifiers, buttons);
	// }

	// revert to View version:
	mouseDownEvent { arg x, y, modifiers, buttonNumber, clickCount;
		if( (modifiers & QKeyModifiers.control) > 0 ) { // if Ctrl / Cmd mod
			// Try to get drag obj and start a drag.
			// If successful, block further processing of this event.
			if( this.beginDrag( x, y ) ) { ^true };
		};
		// else continue to handle mouse down event
		modifiers = QKeyModifiers.toCocoa(modifiers);
		^this.mouseDown( x, y, modifiers, buttonNumber, clickCount );
	}

	mouseMoveEvent { arg x, y, modifiers, buttons;
		if( buttons != 0 ) {
			modifiers = QKeyModifiers.toCocoa(modifiers);
			^this.mouseMove( x, y, modifiers );
		}{
			^this.mouseOver( x, y )
		}
	}
}

