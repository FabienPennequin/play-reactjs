import gulp from 'gulp';
import babel from 'gulp-babel';
import browserify from 'browserify';
import babelify from 'babelify';
import runSequence from 'run-sequence';
import source from 'vinyl-source-stream';
import uglify from 'gulp-uglify';
import rename from 'gulp-rename';
import concat from 'gulp-concat';
import streamify from 'gulp-streamify';
import del from 'del'

const paths = {
  'src': './src/js/',
  'vendor': './node_modules/',
  'dist': {
    'public': './../public/generated/js/',
    'server': './../javascripts/src/main/resources/js/'
  }
};

//
// Js For Browser
//

gulp.task('build-js-browser', function () {
  return browserify({entries: paths.src + 'app.js'})
    .transform(babelify)
    .bundle()
    .pipe(source('app.js'))
    .pipe(gulp.dest(paths.dist.public))
    .pipe(rename({suffix: '.min'}))
    .pipe(streamify(uglify({mangle: false, preserveComments: 'some'})))
    .pipe(gulp.dest(paths.dist.public))
});

//
// Js For Server
//

gulp.task('build-js-server-deps', function() {
  return gulp.src([
      paths.vendor + 'react/dist/react.js'
    ])
    .pipe(gulp.dest(paths.dist.server));
})
gulp.task('build-js-server-app', function () {
  return gulp.src([
      paths.src + 'components/*'
    ])
    .pipe(babel({modules: 'ignore'}))
    .pipe(concat('server.js'))
    .pipe(gulp.dest(paths.dist.server));
});

gulp.task('build-js-server', function (cb) {
  runSequence('build-js-server-app', 'build-js-server-deps', cb);
});

gulp.task('build-js', function (cb) {
  runSequence('build-js-browser', 'build-js-server', cb);
});

// --- Clean all generated files
gulp.task('clean', function () {
  del([
    paths.dist.public + '*',
    paths.dist.server + '*'
  ], {force: true})
})

// --- WATCH
gulp.task('watch', ['clean', 'build-js'], function() {
  gulp.watch(paths.src + '**.js', ['build-js']);
});

// --- DEFAULT
gulp.task('default', ['clean', 'build-js']);
