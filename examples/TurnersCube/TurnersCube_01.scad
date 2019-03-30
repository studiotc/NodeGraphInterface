function fitRadius(radius) = sqrt((pow(radius, 2)) / 2);
module boringShapes(radius, edge, level) {
	fitRad = fitRadius(radius);
	depth = fitRad + edge;
	translate ([0,0,depth] ) {
		cylinder(r1 = radius, r2 = radius, h = 10, center = false , $fn = 24, $fa = 3, $fs = 1 );
	}
	
	if(level > 0) {
		boringShapes(fitRad, edge, level - 1);
	}
	else {
		union(){
			cylinder(r1 = fitRad, r2 = fitRad, h = 10, center = false , $fn = 24, $fa = 3, $fs = 1 );
			children();
		}
	}
	
}

difference(){
	cube([4.5, 4.5, 4.5], center = true );
	for( i = [[0, 0, 0], [90, 0, 0], [180, 0, 0], [270, 0, 0], [0, 90, 0], [0, -90, 0]] ) {
		rotate (i) {
			boringShapes((4.5 / 2) - 0.125, 0.125, 4);
		}
	}
}

