/// This function returns a string that says "Hello, world!"
///
/// # Examples
/// ```
/// let result = hello_world();
/// assert_eq!(result, "Hello, world!");
/// ```
///
/// # Returns
/// A string that says "Hello, world!"
fn hello_world() -> String {
    let hello: &str = "Hello, ";
    let world: &str = "world!";
    hello.to_string() + world
}

fn main() {
    println!("{}", hello_world());
}
