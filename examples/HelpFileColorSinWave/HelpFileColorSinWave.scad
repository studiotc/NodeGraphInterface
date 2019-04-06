for( i = [0 : 36] ) {
	for( j = [0 : 36] ) {
		 color( c = [0.5+sin(10*i)/2, 0.5+sin(10*j)/2, 0.5+sin(10*(i+j))/2], alpha = 1) {
			translate ([i,j,0] ) {
				cube([1, 1, 11+10*cos(10*i)*sin(10*j)], center = false );
			}
		}
	}
}
