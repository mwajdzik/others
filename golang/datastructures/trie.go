package datastructures

const AlphabetSize = 26

type trieNode struct {
	children [AlphabetSize]*trieNode
	isWord   bool
}

type trie struct {
	root *trieNode
}

func (t *trie) buildNode() *trieNode {
	node := trieNode{}
	node.isWord = false
	node.children = [AlphabetSize]*trieNode{}
	return &node
}

func (t *trie) visitNode(node *trieNode, prefix string, results *[]string) {
	if node.isWord {
		*results = append(*results, prefix)
	}

	for index, child := range node.children {
		if child != nil {
			prefix += string(rune(index + 'a'))
			t.visitNode(child, prefix, results)
		}
	}
}

func (t *trie) AddWord(word string) {
	n := t.root

	for i, c := range word {
		isLast := i == len(word)-1
		index := byte(c) - 'a'

		if n.children[index] == nil {
			newNode := t.buildNode()
			n.children[index] = newNode
			n = newNode
		} else {
			n = n.children[index]
		}

		if isLast {
			n.isWord = true
		}
	}
}

func (t *trie) GetWords(prefix string) []string {
	n := t.root

	for _, c := range prefix {
		pos := c - 'a'

		if n.children[pos] == nil {
			return []string{}
		} else {
			n = n.children[pos]
		}
	}

	var result []string
	t.visitNode(n, prefix, &result)
	return result
}

func NewTrie() *trie {
	t := &trie{}

	t.root = t.buildNode()

	return t
}
